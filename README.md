> [!IMPORTANT]
> This project is archived and no longer maintained. Check out the official [Java Pub/Sub API client example](https://github.com/forcedotcom/pub-sub-api/tree/main/java).


# Sample Java gRPC client for the Salesforce Pub/Sub API

See the [official Pub/Sub API repo](https://github.com/developerforce/pub-sub-api) for more information on the Pub/Sub API.

## Installation

Create a `config.properties` file at the root of the project:

```properties
loginUrl=https://login.salesforce.com
user.username=YOUR_SALESFORCE_USERNAME
user.password=YOUR_SALESFORCE_PASSWORD
user.token=YOUR_SALESFORCE_USER_TOKEN

pubSub.endpoint=api.pubsub.salesforce.com:7443
pubSub.topicName=/data/AccountChangeEvent
pubSub.eventReceiveLimit=1
```

> **Warning**
> This project relies on a username/password Salesforce authentication flow. This is only recommended for test purposes. Consider switching to JWT auth for extra security.

If using a Change Data Capture topic (like in the sample config), make sure to activate the event in Salesforce Setup > Change Data Capture.

Install the project with Maven by running: `mvn install`.

## Execution

Import the project in your favorite Java IDE and run the `org.salesforce.demo.PubSubApiClient` class.

If everything goes well, you'll see output like this:

```
Aug 02, 2022 1:35:06 PM org.salesforce.demo.PubSubApiClient connect
INFO: PubSub API: retrieving Salesforce session...
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Aug 02, 2022 1:35:06 PM org.salesforce.demo.auth.AuthenticationHelper login
INFO: Logged as trail@pozil.com on https://pozil-dev-ed.my.salesforce.com
Aug 02, 2022 1:35:06 PM org.salesforce.demo.PubSubApiClient connect
INFO: PubSub API: connecting to api.pubsub.salesforce.com:7443...
Aug 02, 2022 1:35:07 PM org.salesforce.demo.PubSubApiClient retrieveTopic
INFO: PubSub API: retrieving topic /data/AccountChangeEvent...
Aug 02, 2022 1:35:11 PM org.salesforce.demo.PubSubApiClient retrieveTopicSchema
INFO: PubSub API: retrieving schema for topic /data/AccountChangeEvent...
Aug 02, 2022 1:35:12 PM org.salesforce.demo.PubSubApiClient subscribe
INFO: PubSub API: subscribing to /data/AccountChangeEvent and waiting for 1 events...
```

At this point the script will be on hold and will wait for events.
Once it receives events, it will display them like this:

```
Aug 02, 2022 1:36:08 PM org.salesforce.demo.PubSubEventObserver onNext
INFO: Next event: events {
  event {
    id: "7cb562f2-c7aa-427d-99eb-f2a93b11cdd6"
    schema_id: "vjfSL_rX8hSnqyn0Yla8Zw"
    payload: "\016Account\002$0014H00002LbR7QQAV\000\002hcom/salesforce/api/soap/55.0;client=SfdcInternalAPI/H0000c8a0-6766-3414-949f-de10519bee7d\002\220\302\273\345\313`\342\205\265\215\337\223\005$00558000000yFyDAAU\000\000\004\0200x400000\f4-0x01\000\000\000\000\002\002\03412 Main Street\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\002\220\302\273\345\313`\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000"
  }
  replay_id: "\000\000\000\000\001\004\266t"
}
latest_replay_id: "\000\000\000\000\001\004\266t"
rpc_id: "db6292ab-3533-45d4-84fc-47c5f47480b3"

Aug 02, 2022 1:36:08 PM org.salesforce.demo.PubSubEventObserver onNext
INFO: Event raw payload: {"ChangeEventHeader": {"entityName": "Account", "recordIds": ["0014H00002LbR7QQAV"], "changeType": "UPDATE", "changeOrigin": "com/salesforce/api/soap/55.0;client=SfdcInternalAPI/", "transactionKey": "0000c8a0-6766-3414-949f-de10519bee7d", "sequenceNumber": 1, "commitTimestamp": 1659440165000, "commitNumber": 11334298542449, "commitUser": "00558000000yFyDAAU", "nulledFields": [], "diffFields": [], "changedFields": ["0x400000", "4-0x01"]}, "Name": null, "Type": null, "ParentId": null, "BillingAddress": {"Street": "12 Main Street", "City": null, "State": null, "PostalCode": null, "Country": null, "StateCode": null, "CountryCode": null, "Latitude": null, "Longitude": null, "Xyz": null, "GeocodeAccuracy": null}, "ShippingAddress": null, "Phone": null, "Fax": null, "AccountNumber": null, "Website": null, "Sic": null, "Industry": null, "AnnualRevenue": null, "NumberOfEmployees": null, "Ownership": null, "TickerSymbol": null, "Description": null, "Rating": null, "Site": null, "OwnerId": null, "CreatedDate": null, "CreatedById": null, "LastModifiedDate": 1659440165000, "LastModifiedById": null, "Jigsaw": null, "JigsawCompanyId": null, "CleanStatus": null, "AccountSource": null, "DunsNumber": null, "Tradestyle": null, "NaicsCode": null, "NaicsDesc": null, "YearStarted": null, "SicDesc": null, "DandbCompanyId": null, "CustomerPriority__c": null, "SLA__c": null, "Active__c": null, "NumberofLocations__c": null, "UpsellOpportunity__c": null, "SLASerialNumber__c": null, "SLAExpirationDate__c": null, "Potential_Value__c": null, "Match_Billing_Address__c": null, "Number_of_Contacts__c": null, "Region__c": null}
Aug 02, 2022 1:36:08 PM org.salesforce.demo.PubSubEventObserver onNext
INFO: Event replay ID: 17086068
Aug 02, 2022 1:36:08 PM org.salesforce.demo.PubSubEventObserver onNext
INFO: UPDATE operation on Account with record ID 0014H00002LbR7QQAV
Aug 02, 2022 1:36:08 PM org.salesforce.demo.PubSubEventObserver onNext
INFO: Changed fields: LastModifiedDate,BillingAddress.Street
```

Note that the event payload includes all object fields but fields that haven't changed are null.
Use the values from `ChangeEventHeader.nulledFields`, `ChangeEventHeader.diffFields` and `ChangeEventHeader.changedFields` to identify actual value changes.

After receiving the number of requested events (see `pubSub.eventReceiveLimit`), the script will terminate with these messages:

```
Aug 02, 2022 1:37:09 PM org.salesforce.demo.PubSubEventObserver onCompleted
INFO: Done receiving events.
Aug 02, 2022 1:37:09 PM org.salesforce.demo.PubSubApiClient disconnect
INFO: PubSub API: disconnecting...
Aug 02, 2022 1:37:09 PM org.salesforce.demo.PubSubApiClient main
INFO: PubSub API: client disconnected.
```
