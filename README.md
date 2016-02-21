# Wiki-Payamak
 Wiki-Payamak is a solution that creates a place to share selective textual contents and enables the community to collaborate through selection, edition, and revision of contents. The whole idea is to share valuable, reliable and classified contents with an open community subscribed to each class. 

__WARNING: Wiki-Payamak is in early alpha. There may be bugs and security risks involved in running it on your web server. PROCEED AT YOUR OWN RISK!__
## How it works
You start sharing a content of your chose either by posting it through the web application or [Android Client] (https://github.com/rad-navid/wiki-payamak-android-client). Once you post it, the creation event propagate through the system. If users approved the corresponding notification, the content would be persisted and would be accessible publicly. Any changes made to public contents such as changes in category or content itself (or any other changes available) would be published as a notification and changes would be made base on general approval of community.

## Online Demo
A demo instance is accessible on [Wiki Payamak Demo] (http://wikiwebsite-payamakonline.rhcloud.com/).

*Your first request may get response with some delay because of [Application Idling] (https://developers.openshift.com/en/managing-idling.html).*

##Behind the scenes
Published on awesome [Red Hat's Platform-as-a-Service (PaaS) OpenShift] (https://www.openshift.com/index.html) and is available online at [http://www.wiki-sms.com/] (http://www.wiki-sms.com/)


Used technologies:

* Java EE / JSF / PrimeFaces
* MySQL / Hibernate
* REST API written in Java using Jersey
* GZIP
* Maven
* GIT / EGIT
* HTML / CSS
* JavaScript / jQuery 
* JSON (Gson)
