ands-rifcs-builder
==================

This is a java builder (ideally a static builder within the ANDS-RIFCS-API project itself would be more ideal) for use with ANDS-RIFCS-API. It provides a separate builder and proxy to:
* abstract away more of boilerplate required for rifcs elements
* provide a common registry interface to minimise repetition across registry objects which share common methods.

(Tested against redbox project with groovy and spock: see https://bitbucket.org/qcifltd/rifcs)