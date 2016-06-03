/*
 *
 *  Copyright (C) 2016 Queensland Cyber Infrastructure Foundation (http://www.qcif.edu.au/)
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License along
 *    with this program; if not, write to the Free Software Foundation, Inc.,
 *    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package au.com.redboxresearchdata.scripts

import au.com.redboxresearchdata.service.rifcs.builder.impl.RifcsCollectionBuilder
import au.com.redboxresearchdata.service.rifcs.builder.impl.RifcsGenericBuilder
import au.com.redboxresearchdata.service.rifcs.builder.sub.impl.RifcsGenericSubBuilder
import com.googlecode.fascinator.api.storage.DigitalObject
import com.googlecode.fascinator.common.StorageDataUtil
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.ands.rifcs.base.RIFCSWrapper
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.commons.lang3.StringUtils

import javax.xml.XMLConstants
import javax.xml.bind.DatatypeConverter
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator

/**
 * Builder to wrap <a href="https://github.com/au-research/ANDS-RIFCS-API.git">ANDS-RIFCS-API</a>
 * @version
 * @author <a href="matt@redboxresearchdata.com.au">Matt Mulholland</a>
 */

//def log = LoggerFactory.getLogger(ScriptRunner.class)
//log.info("TfpackageToRifcs script starting...")

return new TfpackageToRifcs(digitalObject, config).transform()

// convenience method
static def createInstance(digitalObject, config) {
    return new TfpackageToRifcs(digitalObject, config)
}

@Slf4j
class TfpackageToRifcs {
    final DigitalObject digitalObject
    final def config
    final def metadata
    final def tfpackage

    TfpackageToRifcs(final DigitalObject digitalObject, final String config) {
        this.digitalObject = digitalObject
        this.config = parseJson(config)
        this.metadata = digitalObject.getMetadata()
        this.tfpackage = parseJson(getTfpackage(digitalObject))
    }

    def parseJson(text) {
        JsonSlurper slurper = new JsonSlurper()
        log.info 'slurping text...'
        def json = slurper.parseText(text)
        log.debug("Printing json text as groovy json...")
        log.trace(json.toString())
        return json
    }

    def prettyPrint(RIFCSWrapper data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        data.write(baos)
        log.debug("returning pretty print format for ${data.class} ...")
        return baos.toString()
    }

    def getTfpackage(DigitalObject digitalObject) {
        def tfpackageKey = digitalObject.getPayloadIdList().find { it =~ /.*\.tfpackage/ }
        log.debug("tf package key is " + tfpackageKey)
        def tfpackage = digitalObject.getPayload(tfpackageKey).open().text
        log.trace("tfpackage text is: " + tfpackage)
        return tfpackage
    }

    def parseDate(String date) {
        if (StringUtils.isNotBlank(date)) {
            return DatatypeConverter.parseDateTime(date).getTime()
        }
    }

    Expando createIdentifierData() {
        def metadataPid = config.curation?.pidProperty ? digitalObject?.getMetadata().get(config.curation.pidProperty) : null
        String pid = metadataPid ?: tfpackage.metadata?.'rdf:resource' ?: tfpackage.metadata?.'dc.identifier'
        log.debug("pid is: " + pid)

        def expando = new Expando()
        if (tfpackage.'dc:identifier.redbox:origin' == 'internal') {
            expando.identifier = pid ?: config.'urlBase' + "/detail/" + metadata.get("objectId")
            expando.identifierType = pid ? StringUtils.defaultIfBlank(config.curation?.pidType, "&Invalid XML placeholder... prevents ANDS Harvesting records in error&") : "uri"
        } else {
            expando.identifier = tfpackage.'dc:identifier.rdf:PlainLiteral' ?: "&Invalid ID: Not curated yet&"
            expando.identifierType = tfpackage.'dc:identifier.rdf:PlainLiteral' ? tfpackage.'dc:identifier.dc:type.rdf:PlainLiteral' : "invalid"
        }
        if (!expando.identifierType) {
            log.warn("No identifier type set for identifier: " + expando.identifier)
        }
        log.debug("identifier is: " + expando.identifier)
        log.debug("identifier type is: " + expando.identifierType)
        return expando
    }

    def getAllRelations() {
        def relations = tfpackage.relationships
        log.trace("relations are: " + relations)
        def collected = relations.findAll {
            log.debug("next relation: " + it)
            it.isCurated && it.curatedPid?.trim()
        }.collect {
            [key: it.curatedPid, type: it.relationship ?: "hasAssociationWith", description: it.description]
        }
        log.debug("relations collected: " + collected)
        def grouped = collected.plus(getAllNlaRelations()).groupBy { it.key }
        log.debug("grouped: " + grouped)
        return grouped
    }

    def getAllElectronicAddress() {
        return findAndCollect(getTfpackageNumberedCollection("bibo:Website"), 'dc:identifier')
    }

    def getAllNlaRelations() {
        return findAndCollect(getTfpackageNumberedCollection("dc:creator.foaf:Person"),
                { k, v -> v.'dc:identifier'?.trim() && v.'dc:identifier' =~ /http:\/\/nla.gov.au\/nla.party-/ },
                { k, v -> [key: v.'dc:identifier', type: "hasCollector"] }
        )
    }

    def getAllGeoSpatialCoverage() {
        return findAndCollect(getTfpackageNumberedCollection("dc:coverage.vivo:GeographicLocation"),
                { k, v ->
                    log.debug("next key: ${k} has value: ${v}")
                    v.'rdf:PlainLiteral'?.trim()
                },
                { k, v ->
                    def first = [[type: v.'dc:type', value: v.'rdf:PlainLiteral']]
                    if (!(v.'redbox:wktRaw'?.trim())) {
                        return v.'dc:type'.trim() == 'text' ? first <<
                                [type: "dcmiPoint", value: "name="
                                        + v.'rdf:PlainLiteral'
                                        + "; east="
                                        + v.'geo:long'
                                        + "; north="
                                        + v.'geo:lat'
                                        + "; projection=WGS84"
                                ]
                                : first
                    } else if (v.'dc:type') {
                        first
                    } else {
                        log.debug("redbox:wktRaw is not empty, but there is no type present. No geo spatial coverage collected.")
                    }
                }).flatten()
    }

    def getAllSubjects() {
        def subjects = findAndCollect(getTfpackageNumberedCollection("dc:subject.vivo:keyword"), "rdf:PlainLiteral",
                { k, v ->
                    [value: v.'rdf:PlainLiteral', type: "local"]
                })
                .plus(
                findAndCollect(getTfpackageNumberedCollection("dc:subject.anzsrc:for"), "rdf:resource",
                        { k, v ->
                            [value: ("${v.'rdf:resource'}".replaceAll(~".*[/]", "")), type: "anzsrc-for"]
                        }))
                .plus(
                findAndCollect(getTfpackageNumberedCollection("dc:subject.anzsrc:seo"), "rdf:resource",
                        { k, v ->
                            [value: ("${v.'rdf:resource'}".replaceAll(~".*[/]", "")), type: "anzsrc-seo"]
                        }))
        return tfpackage.'dc:subject.anzsrc:toa.skos:prefLabel' ? subjects.plus([value: tfpackage.'dc:subject.anzsrc:toa.skos:prefLabel', type: "anzsrc-toa"]) : subjects
    }

    def getAllRelatedInfo() {
        return getRelatedInfo("dc:relation.swrc:Publication", "publication", "uri").
                plus(getRelatedInfo("dc:relation.bibo:Website", "website", "uri")).
                plus(getRelatedInfo("dc:relation.vivo:Service", "service", "uri")).flatten()
    }

    def getRelatedInfo(def field, def type, def identifierType) {
        return findAndCollect(getTfpackageNumberedCollection(field), "dc:identifier",
                { k, v ->
                    if ((v.'dc:identifier')?.trim()) {
                        def returned = [identifier: v.'dc:identifier', type: type, identifierType: identifierType]
                        if ((v.'dc:title')?.trim()) {
                            returned << [title: v.'dc:title']
                        }
                        return returned
                    }
                })
    }

    def getRelatedObject(def field) {
        return findAndCollect(getTfpackageNumberedCollection(field), "dc:identifier",
                { k, v ->
                    if ((v.'dc:identifier')?.trim()) {
                        def returned = [key: v.'dc:identifier']
                        if ((v.'vivo:Relationship.rdf:PlainLiteral')?.trim()) {
                            returned << [type: v.'vivo:Relationship.rdf:PlainLiteral']
                        }
                        return returned
                    }
                })
    }

    def getCitation(def identifierData) {
//        <citationInfo> element should contain either <fullCitation> or <citationMetadata> (not both)
        if (tfpackage.'dc:biblioGraphicCitation.redbox:sendCitation'?.trim() == "on") {
            String value = tfpackage.'dc:biblioGraphicCitation.skos:prefLabel'
            def doi = config?.andsDoi?.doiProperty ? metadata.get(config.andsDoi.doiProperty) : null
            if (value?.trim()) {
                // send citation info value
                return [style: "Datacite", citation: value.replaceAll(/\{ID_WILL_BE_HERE\}/, doi ? "http://dx.doi.org/" + doi : identifierData.identifier)]
            }
        }
    }


    def getDescription() {
        if (tfpackage.'dc:description') {
            return [value: tfpackage.'dc:description', type: "full"]
        }
    }

    def getAccessRights() {
        if (tfpackage.'dc:accessRights.skos:prefLabel'?.trim()) {
            return tfpackage.'dc:accessRights.dc:identifier' ? [value: tfpackage.'dc:accessRights.skos:prefLabel', uri: tfpackage.'dc:accessRights.dc:identifier'] :
                    [value: tfpackage.'dc:accessRights.skos:prefLabel']
        }
    }

    def getLicence() {
        if (tfpackage.'dc:license.skos:prefLabel'?.trim() && tfpackage.'dc:license.dc:identifier'?.trim()) {
            return [value: tfpackage.'dc:license.skos:prefLabel', uri: tfpackage.'dc:license.dc:identifier']
        } else if (tfpackage.'dc:license.rdf:Alt.skos:prefLabel'?.trim()) {
            return tfpackage.'dc:license.rdf:Alt.dc:identifier' ? [value: tfpackage.'dc:license.rdf:Alt.skos:prefLabel', uri: tfpackage.'dc:license.rdf:Alt.dc:identifier'] :
                    [value: tfpackage.'dc:license.rdf:Alt.skos:prefLabel']
        } else {
            return null
        }
    }

    def getRightsStatement() {
        if (tfpackage.'dc:accessRights.dc:RightsStatement.skos:prefLabel'?.trim()) {
            return tfpackage.'dc:accessRights.dc:RightsStatement.dc:identifier' ? [value: tfpackage.'dc:accessRights.dc:RightsStatement.skos:prefLabel', uri: tfpackage.'dc:accessRights.dc:RightsStatement.dc:identifier'] :
                    [value: tfpackage.'dc:accessRights.dc:RightsStatement.skos:prefLabel']

        }
    }

    def getTfpackageNumberedCollection(String listCriteria) {
        return new StorageDataUtil().getList(tfpackage, listCriteria)
    }

    def findAndCollect(def data, String findCriteria) {
        findAndCollect(data, findCriteria, findCriteria)
    }


    def findAndCollect(def data, String findCriteria, String collectCriteria) {
        findAndCollect(data,
                { k, v -> v[(findCriteria)]?.trim() },
                { k, v -> v[(collectCriteria)] })
    }

    def findAndCollect(def data, String findCriteria, Closure collectCriteria) {
        findAndCollect(data,
                { k, v -> v[(findCriteria)]?.trim() },
                collectCriteria)
    }

    def findAndCollect(def data, Closure findCriteria, Closure collectCriteria) {
        log.debug("data to search is: " + data)
        def collected = data.findAll(findCriteria).collect(collectCriteria)
        log.debug("collected: " + collected)
        return collected
    }


    def addNonEmpty = { methodName, value ->
        if (value) {
            return delegate."${methodName}"(value)
        }
        return delegate
    }

    def addEveryNonEmpty = { methodName, values ->
        values.each {
            if (it) {
                owner.delegate = owner.delegate."${methodName}"(it)
            }
        }
        return delegate
    }

    def addEveryNonEmptyMap = { methodName, values ->
        values.each { k, v ->
            if (k) {
                owner.delegate = owner.delegate."${methodName}"(k, v)
            }
        }
        return delegate
    }

    def transform() {
        def identifierData = createIdentifierData()

        RIFCSWrapper.metaClass.validate = {
            // ensures dependencies do not override schema factory library that works with ANDS library
            SchemaFactory factory = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI, "com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory", null)
            Schema schema = factory.newSchema(doXercesWorkaround())

            Validator validator = schema.newValidator()
            validator.validate(new DOMSource(doc))
        }

        RifcsGenericBuilder.metaClass.addNonEmpty = addNonEmpty
        RifcsGenericBuilder.metaClass.addEveryNonEmpty = addEveryNonEmpty
        RifcsGenericBuilder.metaClass.addEveryNonEmptyMap = addEveryNonEmptyMap
        RifcsGenericSubBuilder.metaClass.addNonEmpty = addNonEmpty
        RifcsGenericSubBuilder.metaClass.addEveryNonEmpty = addEveryNonEmpty
        RifcsGenericBuilder.metaClass.buildLocation = {
            return delegate.locationBuilder()
                           .addNonEmpty('physicalAddress', tfpackage.'vivo:Location.vivo:GeographicLocation.gn:name')
                           .addEveryNonEmpty('urlElectronicAddress', getAllElectronicAddress())
                           .build()
        }
        RifcsGenericBuilder.metaClass.buildTemporalCoverage = {
            return delegate.temporalCoverageBuilder()
                           .addNonEmpty('coverageDateFrom', parseDate(tfpackage.'dc:coverage.vivo:DateTimeInterval.vivo:start'))
                           .addNonEmpty('coverageDateTo', parseDate(tfpackage.'dc:coverage.vivo:DateTimeInterval.vivo:end'))
                           .addNonEmpty('coveragePeriod', parseDate(tfpackage.'dc:coverage.redbox:timePeriod'))
                           .build()
        }
        RifcsGenericBuilder.metaClass.buildSpatialCoverage = {
            return delegate.spatialCoverageBuilder()
                           .addEveryNonEmpty('spatial', getAllGeoSpatialCoverage())
                           .build()
        }
        RifcsGenericBuilder.metaClass.buildRights = {
            return delegate.rightsBuilder()
                           .addNonEmpty('accessRights', getAccessRights())
                           .addNonEmpty('licenceRights', getLicence())
                           .addNonEmpty('rightsStatement', getRightsStatement())
                           .build()
        }

        def rifcs = new RifcsCollectionBuilder(identifierData.identifier, config.urlBase, config.identity.RIF_CSGroup, tfpackage.'dc:type.rdf:PlainLiteral')
                .identifierType(identifierData.identifier, identifierData.identifierType)
                .addNonEmpty('dateModified', parseDate(tfpackage.'dc:modified'))
                .addNonEmpty('dateAccessioned', parseDate(tfpackage.'dc:created'))
                .addEveryNonEmptyMap('relatedObjects', getAllRelations())
                .addNonEmpty('primaryName', tfpackage.'dc:title')
                .buildLocation()
                .buildTemporalCoverage()
                .buildSpatialCoverage()
                .addEveryNonEmpty('subject', getAllSubjects())
                .addNonEmpty('description', getDescription())
                .buildRights()
                .addEveryNonEmpty('relatedInfo', getAllRelatedInfo())
                .addNonEmpty('relatedObject', getRelatedObject("dc:relation.vivo:Service"))
                .addNonEmpty('fullCitation', getCitation(identifierData))
                .build()

        def prettyPrintedRifcs = prettyPrint(rifcs)
        log.info("pre-validation au.com.redboxresearchdata.rifcs transformation: " + prettyPrintedRifcs)
        rifcs.validate()
        return prettyPrintedRifcs
    }

}