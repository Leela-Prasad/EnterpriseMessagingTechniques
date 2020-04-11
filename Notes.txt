Source Code Link:
http://www.wmrichards.com/emvideocode.html

Why Messaging?
1. For Heterogeneous/Homogeneous Interoperability 
   communication between Java and .Net Apps or Java and Mainframe Apps
2. Component Decoupling
   Messaging helps us to decouple services/components so that they don't know who is the subscriber and who is consumer, and the components communicate via Message Channels.
3. High Scalability through Load Balancing
   We can add instances to the cluster for scaling applications.
4. Asynchronous Capabilities (Fire and Forget)
   Sender doesn't need to wait for the response and can process other tasks in parallel.
5. Guaranteed Delivery of Messages
   Messages will Never be lost as it persists messages to the disk.

Messaging Models
1. Point to Point 
2. Publish Subscribe (PubSub) 

Point to Point
1. Uses queues, senders and receivers.
2. Message delivered to ONLY ONE consumer.
3. supports fire-and-forget and request-reply messaging.
   In fire and forget we can send a message to the queue and process other tasks.
   In request reply messaging we do a blocking wait for the response
   eg: gettting customer info
       placing a trade and waiting for the confirmation.
4. supports load balancing of receivers. 
   We can have n number of nodes behind the load balancer this will help to process n messages at the same time which increases throughput.
5. When the receiver is busy then messages will be queued up and this is called as queue depth.
6. We can browse a queue.

Publish Subscribe
1. Uses topics, Publishers and Subscribers.
2. Message delivered to EACH subscriber.
3. supports fire-and-forget broadcast notification.
4. supports load balancing of subscribers(ONLY FROM JMS 2.0)
5. In this Model Publisher doesn't know Subscribers.
eg: price of apple stock changes we can publish a message and we don't know who will receive that message.
6. When subscribers are busy
We can visualize topic having virtual queues for every subscriber, so when sub1 and sub2 are busy then the virtual queues will get piled up and will be sent when the subscribers have bandwidth to process messages.
This Virtualization is same as Bridges in Tibco EMS.
7. We cannot browse a topic.


Message Structure
It contains 2 parts
1. Header
2. Body

1. Header has below divisions
a) JMS Standard Header Properties.
b) JMS Extended Header Properties(Added as part of JMS 1.1 and JMS 2.0).
c) Application Specific Header Properties.
d) Vendor Specific Header Properties - These are needed for internal broker implementation.

2. Body
It just a payload and this payload can be one of below types.
Types of Payloads
a) Text Payload
b) java object Payload
c) Map Payload
d) Byte Payload
e) Stream Payload
f) Event Payload


Guaranteed Delivery:
Guaranteed Delivery requires message persistence (which is default) i.e., ONLY Persistence Messages Survive System failures.
There is a performance hit when using Guaranteed Delivery.
Here Persistence can be done via File System or a RDBMS

Below is Flow of a message persistence for Happy Path.
1. sender sends a message to JMS Provider.
2. Persist Message to Persistence store.
3. JMS Provider Acknowledge Sender.

4. Deliver Message to Consumer.
5. Acknowledge JMS Provider.
6. Deletes Message from Persistence Store.

Message Persistence Flow for a System Failure Before Acknowledgement
1. Sender sends a message to JMS Provider
2. JMS Provider is down.
3. JMS Provider sends Exception to the sender.

Message Persistence Flow for a System Failure After Acknowledgement
1. sender sends a message to JMS Provider.
2. Persist Message to Persistence store.
3. JMS Provider Acknowledge Sender.
4. System Failure (JMS Provider is Down).
5. System Restored.
6. Deliver Message to Consumer.
7. Acknowledge JMS Provider.
8. Deletes Message from Persistence Store.

ACTIVEMQ_HOME=/install_location/apache-activemq-5.9.1

*** A session in JMS is a transaction which will define unit of work.
so JMS Sessions is same as Database Transaction
Database Connection is same as JMS Connection
So one JMS Connection will have multiple sessions.


JMS 2.0
API changes:
simiplified api - this is a new one which will have new interfaces for sending and receiving messages in a simple way.
classic api updates - this is JMS 1.1 interfaces with minor updates
MDB config settings - Changes for Message Driven Bean i.e., taking configuration from container level to the annotation (code) level.

New Features:
shared subscriptions - with this we can load balance subscribers in a topic and this load balancing isn't available in JMS 1.1
async send - this will allow to send a message to JMS Provider in Asynchronous way so that we will not wait for the acknowledgment from the JMS provider.
In JMS 1.1 we have Blocking wait on the send method.
Delivery Delay - we can set a Delay so that the message can be delievered after specified amount of time.
Delivery count

Simplified API 
in Simplified API we have below interfaces.
1. JMSContext
2. JMSProducer
3. JMSConsumer

In Simplified API we don't need to create connection factory, connection, session explicitly
we have a method in JMSContext
createContext()

This method will reuse the existing connection factory and connection object BUT NEW SESSION OBJECT WILL BE CREATED.
So if need a transaction/session we need to use createContext which creates a session.

JMSProducer
New Class replaces MessageProducer
In JMSProducer we have below capabilities
Uses a Builder Pattern (DSL Like) 

No Need to create a Message object except for Stream message
eg: jmsContext().createProducer.send(queue, "Test Message")

Since there is no need to create a Message object All the JMS Header properties are moved to JMSProducer
setJMS...()

JMSConsumer
New Class replaces MessageConsumer

Uses a Builder Pattern (DSL Like) 

No Need to create a Message object except for Stream message
eg: String payload = jmsContext().createConsumer(queue).receiveBody(String.class)



sudo yum install java
wget https://archive.apache.org/dist/activemq/activemq-artemis/2.11.0/apache-artemis-2.11.0-bin.tar.gz
tar -xvf apache-artemis-2.11.0-bin.tar.gz
./apache-artemis-2.11.0/bin/artemis create mybroker
cd mybroker/bin
./artemis run



check java if not there then
sudo yum install java

sudo yum install docker

mkdir /home/ec2-user/Artemis
export ARTEMIS_HOME=/home/ec2-user/Artemis

./prepare-docker.sh $ARTEMIS_HOME
cd $ARTEMIS_HOME
docker build -f ./docker/Dockerfile-ubuntu -t artemis-ubuntu .

export ARTEMIS_USER=admin
export ARTEMIS_PASSWORD=admin
export ARTEMIS_PASSWORD=true

docker run -p 61616:61616 -p 8161:8161 artemis-ubuntu 


JMS 2.0 Features:
1. Shared Subscriptions
2. Asynchronous Send
3. Delivery Delay
4. Delivery Count

1. Shared Subscriptions
Upto JMS 1.1 we cannot load balance subscribers that are registered to the topic.
Suppose for a topic if an applicaiton is registered and this application has 4 instances behind Load Balancer then message pubished to the topic will deliver same message to all 4 instances.
For this reason only we never use a subscriber instead we use a bridge so that first message will flow from a topic to a queue and then to one of the instance.

From JMS 2.0 Load Balancing Subscribers capability is available, so in JMS 2.0 message will be sent to one of the instance from Topic to instance.
so in JMS 2.0 there is no need of a bridge.

2. Asynchrounous Send
JMS2.0 provides a capability to send messages without having to wait for broker acknowledgment.

You can send a message and you can parallely execute some of the logic before getting broker acknowledgment. So this will improve some performance if you have some of the logic that can be executed parallely before getting broker acknowledgment.

Non Persistent messages are processed 4 times faster than Persistent messages because of the persistence work that broker need to do to a File system or JDBC.
We can leverage this Asynchronous send capability to improve performance.

3. Delivery Delay
JMS 2.0 allows you to send a message with instructions not to release if for consumption for a specified amount of time.
Here the message is sent to the broker but the message is not available to the consumer until specified period of time.

This Feature we can use to avoid race conditions like
inserting a record into database and publishing to subscribers
First we can insert and with a delay we can publish to subscribers.

If our application has a peak time between 11 to 12 then some of the unimportant messages that we are sending during this time can be set with a delivery dealy of 1 hour so that it will not increase the load on the system.

4. Delivery Count
JMSXDeliveryCount is also present in JMS 1.1 Specification but it is an optional Header, so we need to check whether a specific broker implements this feature or not.

But in JMS 2.0 JMSXDeliveryCount is a required property allowing consumers better control over poision messages and repeated business logic.

Sometimes due to some issues message cannot be processed then JMS Provider will redeliver this message to the consume so this will be in a infinite loop if the issue exist for more time, to control this we can use JMSXDeliveryCount to decide and move it to another queue, so that prod support team will move the message back to the main queue once issue is resolved.
To solve this issue we have Backout Pattern  which is based on JMSXDeliveryCount.


JMS Headers:

1. JMSCorrelationID:
It is a string value used during request/reply processing for the producer to locate the message response.
It is set by developer, Generally set with message Id as it is unique id for a message.

JMS 1.1
msg.setJMSCorrelationID(inMsg.getJMSMessagID())

JMS2.0
jmsContext.createConsumer().setJMSCorrelationID(inMsg.getJMSMessagID())

2. JMSDeliveryMode:
stores an int value indicating whether this message should be persistent or non-persistent, default is persistent.

DeliveryMode.NON_PERSISTENT (1)
DeliveryMode.PERSISTENT (2)

JMS1.1
MessageProducer.setDeliveryMode(deliveryMode)
MessageProducer.send(msg, deliveryMode, priority, expiry)

JMS 2.0
JMSProducer.setDeliveryMode(deliveryMode)

3. JMSExpiration:
contains the timestamp(in milliseconds) that the message should expire and be removed fromt the queue.
Default value is 0, i.e., it will live forever.

JMS1.1
MessageProducer.setTimeToLive(duration)
MessageProducer.send(msg, deliveryMode, priority, duration)

JMS 2.0
JMSProducer.setTimeToLive(duration)

4. JMSMessageID:
contains a vendor specific generated string value uniquely identify the message.
it is set by the message broker when the message is received.

5. JMSPriority:
contains an int value indicating priority of the message.
0-4 is normal priority
5-9 is expedited priority
Default is 4

JMS1.1
MessageProducer.setPriority(priority)
MessageProducer.send(msg, deliveryMode, priority, duration)

JMS 2.0
JMSProducer.setPriority(priority)

6. JMSRedelivered:
contains a boolean value indicating whether the message has already been deliverd to a consumer.

7. JMSTimestamp:
contains time (in milliseconds) when the broker receives the message during send() operation.

8. JMSType:
used by some vendors to store a string value containing the message structure and payload type when communicating with non-jms messaging systems.

msg.setJMSType("xml")

jmsContext.createProducer().setJMSType("xml")

It is a pitfall and is advised to avoid using this header as when communicating with non-jms systems like .NET and the app require the message in bytes form then it will take the message type and convert to bytes form

If we want to give some metadata like payload type it is better to use custom message property like below

msg.setStringProperty("PayloadType", "JSON")

9. JMSDestination:
contains a javax.jms.Destination object representing the queue or topic this message is to be delivered to 

Destination destination = msg.getJMSDetation()
queue://EM_TRADE.Q

10. JMSReplyTo:
contains a javax.jms.Destination object representing the queue or topic the consumer should send a reply message to during request/reply processing.

JMS1.1
msg.setJMSReplyTo(replyQueue)

JMS2.0
jmsContext.createProducer().setJMSReplyTo(replyQueue)

msg.getJMSReplyTo()

11. JMSDeliveryTime:
Only in JMS 2.0
contains time (in milliseconds) that a message should be delivered to the consumer from the broker.
This Property is important when we set a delay in the message, so that broker knows when exactly to make the message available to the consumer.

jmsContext.createProducer().setDeliveryDelay(5000)

long deliveryTime = msg.getJMSDeliveryTime()


JMS Extended Properties:
1. JMSXUserID
contains a string value representing the userid of the messaging client.
msg.getStringProperty("")
msg.getStringProperty("JMSXUserID")

2. JMSXAppID
contains a string value representing the application id of the messaging client.
eg: ReportingApplication
msg.getStringProperty("JMSXAppID")

3. JMSXProducerTXID
contains a unique transaction identity when the message is sent within a transaction
This is Set by Broker
msg.getStringProperty("JMSXProducerTXID")

4. JMSXConsumerTXID
contains a unique transaction identity when the message is received within a transaction
This is Set by Broker
msg.getStringProperty("JMSXConsumerTXID")

5. JMSXRcvTimestamp
contains time (in milli seconds) when the message was received by a consumer
This is Set by Broker
msg.getLongProperty("JMSXRcvTimestamp")

Time taken to consume message = JMSTimestamp - JMSXRcvTimestamp

6. JMSXDeliveryCount
contains an int value indicating number of times a message has been delivered to a consumer without acknowledgment
msg.getIntProperty("JMSXDeliveryCount")

This property is useful 
to identify poison messages
to identify a partially processed message so that we can do a clean up before processing a message.

7. JMSXState
contains an int value indicating interal state of the message.
1 - waiting
2 - ready
3 - expired
4 - retained

msg.getIntProperty("JMSXState")

8. JMSXGroupID
contains a string value identifying the group the message belongs to 
*** All Messages of same group sent to same consumer
set by developer when grouping related messages or using message streaming
msg.setStringProperty("JMSXGroupID", "GROUP-9634132")
jmsProducer.setProperty("JMSXGroupID", "GROUP-9634132")

When there is a scenario where we want to send a bunch of messages in the SAME ORDER then we can uses this JMSXGroupID
This JMSXGroupID sends all the messages with same group id to one consumer/node behind the load balancer
and all the related messages will go in an ORDER.

Because of this Messages with Same Group ID will be processed by ONE NODE, and all other messages are processed by other nodes in the load balancer.

9. JMSXGroupSeq
contains an int value identifying the sequence number for the message
set by developer when grouping related messages or using message streaming

msg.setIntProperty("JMSXGroupSeq", 4)
jmsProducer.setProperty("JMSXGroupSeq", 4)

Application Header Properties:
set via setProperty or set<Type>Property() method
retrieved via get<Type>Property() method

msg.setLongProperty("shares", 1000);

jmsContext.createProducer().setProperty("shares", 1000);

** When using set<Type>Property 
we get Null Pointer Exception if the property doesn't exist.
we get Format Exception if the property is not able to cast
this happens because it does casting internally.

Property names are case sensitive.

property names cannot be one of the following as below are reserved keywords
not
and
or
between
like
in
is
null
true
false

msg.setObjectProperty() can only contain String objects and Primitive wrapper classes
msg.setObjectProperty("obj", new Double(1000));

Below are not allowed
msg.setObjectProperty("obj", new BigDecimal(1000));
msg.setObjectProperty("obj", new Date());
msg.setObjectProperty("obj", new TradeOrder());

Below Conversions are possible between producer and consumer
setProperty    getProperty
boolean  boolean,string
byte  byte,short,int,long,string
short    short,int,long,string
int   int,long,string
long  long,string
float    float,double,String
double   double,String
string   all types

Public/Subscribe:
uses topics, publishers, subscribers
message delivered to each subscriber or shared subscription(jms 2.0)
fire and forget broadcast notification
publisher doesn't know who are its subscribers
Load balancing of subscribers (only in jms 2.0)

when we need to use pub/sub?
Topic is a virtual concept where it will never store messages and broadcast to available subscribers.
when you need to deliver same message to multiple consumers, but Bridges also do the same?
this use case is specific to 
when you don't care who is receiving the message.
when it is ok that the message may not be received (and hence processed) by any consumers may be subscribers are down or selector expression is not satisfied.
when you don't need a reply from consumer.

There are 4 types of subscribers
1. non-durable subscriber
2. durable subscriber
3. non-durable shared subscriber (JMS 2.0)
4. durable shared subscriber (JMS 2.0)

Non-Durable Subscriber - subscribers only receive messages when they are active.
Durable Subscriber - inactive subscribers will receive stored messages when they become active again.

JMS 1.1 Durable Subscribers are identified uniquely by Topic Name, client id and subscription id
Topic topic = Session.createTopic("EM_TRADE.T")
connection.setClientID("client:1")
session.createDurableSubscriber(topic, "sub:1")

so durable subscriber key = EM_TRADE.T,client:1,sub:1

Generally Client ID can be set at broker url level and it makes sense as it will be app id and multiple programs can reuse
Broker Url = tcp://localhost:61616?jms.clientID=client:1

** A durable subscriber will continue to receive messages until it is unsubscribed

In JMS 1.1:
topicSubscriber.close()
session.unsubscribe("sub:1")

In JMS 2.0:
jmsConsumer.close()
jmsContext.unsubscribe("sub:1")


Non Durable Shared Subscriber:
messages are received only as long as one subscriber is active in subscripton(shared group)

In JMS 2.0 classic api
session.createSharedConsumer(topic, "group:1")
topicSession.createSharedSubscriber(topic, "group:1")

In JMS simplified api
jmsContext.createSharedConsumer(topic, "group:1")

In Shared Subscriptions we don't have noLocal flag, and by default it is set to false so the subscribers will receive their own published messages.

Durable Shared Subscriber:
Inactive subscribers within a group will receive stored messages when they become active again.

In JMS 2.0 classic api
session.createSharedDurableConsumer(topic, "group:1")
topicSession.createSharedDurableSubscriber(topic, "group:1")

In JMS 2.0 simplified api
jmsContext.createSharedDurableConsumer(topic, "group:1")

** In JMS 2.0 Durable Shared Subscriber is uniquely identified by topic name and subscription id (client id is not needed)

Topic topic = jmsContext.createTopic("EM_TRADE.T")
jmsContext.createSharedDurableConsumer(topic, "group:1")

So the durable shared subscription key = EM_TRADE.T, group:1



Message Selectors:
Message Selectors are used to selectively receive messages.
Message selectors are used by receiver or subscriber.

selectors are subset of sql-92 where clause expression language
so we can use selector expression which is same as where clause expression in sql.

** Selectors must be message header properties ONLY.

eg:
JMSPriority > 4
Symbol = 'AAPL' and Shares > 1000
Commission > (Shares * Prices * 0.02)

Selector expression has below fields
1. Identifiers
2. Literals
3. operators

Symbol = 'AAPL' and Shares > 1000
Identifiers: Symbol, Shares
Literals: AAPL, 1000
Operators: and, >

Identifier:
Identifier must be exact match(case sensitive) of header property
identifiers cannot be 
not, is, between, false, or, true, in, like, and, null

Literal:
contains numbers, strings and boolean values

** string must be enclosed in single quotes
Symbol = 'AAPL' and Trader = 'O''Brien'
Here we escape 'Brien with another '

boolean values are true and false

Operators:
logical operators: and, or
algebraic operators: =, <, >, <=, >=, <>
arithmetic operators: +,-,*,/
comparision operators: like, between, in, not, is null, is not null

eg:
JMSPriority in (5,6,7)
Symbol like '_AA' or Symbol like '%BB'
Here _ means one single character, it can be any character
Commission > (Shares * Prices * 0.02)
Price between 90.50 and 94.60 (inclusive)


Message Selectors creates a tight coupling between producer and consumer

Design Considerations:
with point to point messaging there is high possibility of stuck messages because selector expression is not satisfied
This will cause some of the messages piled up in the queue.

To solve this we should have opposite selector expressions also, and we need to write expressions to cover 100% receiving capability.
eg:
Shares > 1000
for this we should also have
Shares < 1000
Shares = 1000

Message Filtering vs Multiple Destinations

Message Filtering:
Consumer Driven Filtering
More Stuck Messages
Poor load balancing as most of the message can go through one filter due to filter condition.

                         -filter->  Queue Receiver
Queue Sender  ->  Queue  -filter->  Queue Receiver
                         -filter->  Queue Receiver


Mutliple Destinations (Bridges in Tibco EMS)
Producer Driven Filtering
fewer stuck messages
Better Load Balancing

              ->  Queue -> Queue Receiver
Queue Sender  ->  Queue -> Queue Receiver
              ->  Queue -> Queue Receiver


JMS Acknowledge Modes:
1. Auto Acknowledge
2. Client Acknowledge
3. Dups Ok Acknowledge

Auto Acknowlegement:
Automatically marks the message as delivered when received by the consumer.
suppose if we get an exception in the consumer during message processing then that message is lost.
deliver
   |
delivered 
   |
acknowledged
   |
removed              

Here Delivered means that the message is locked and it is not available to other consumers.


Client Acknowlegement:
Marks Message as delivered only when manually acknowledged by consumer

Dups ok Acknowledgement
It is same as Auto Acknowledgement but it is lazily mark the message as delivered when received by consumer.

Lazily marking the message means that message is NOT LOCKED, so it will be available to other consumers hence message will be delivered multiple/duplicate times.

This is very rarely used.

Messaging Topologies:
1. Internal Broker
2. External Broker
3. Embedded Broker

Internal Broker:
In this jms broker is inside the Applicaiton Server
Disadvantages:
This topology allows ONLY jms messaging (no heterogenous messaging)
Because Application and jms broker run on the same Application server below will be issues.
1. separation of concerns 
Application and jms broker functionality run on same server
suppose if we want to bring the Application down for maintenance then jms broker will be down and any other applications using this broker will not be able to access jms broker.

2. reliability and performance issues
Messaging requires extensive cpu and threads to process, so there might be resources not available for our application.

External Broker:
In this jms broker is installed on a separate machine
support for heterogenous messaging as it is not coupled with Applicaiton server such as websphere, weblogic, jboss etc
We can also do clustering, scaling on this broker. No resource issue.

3. Embedded Broker:
This broker will be own by the Applicaiton we are running and administered by the application.
useful when you need localized messaging and doesn't need configuration for external broker.


Queue Design Considerations:
1. Queue-Router Design
2. Queue-Processor Design

Queue Router Design:
In Queue Router Design we have a router and the processors
here processors are just a pojo means it is not message aware
when a message arrive into the queue then the router will route the message to a specific processor for processing the message.

This Design can make some of the order types to wait as other type of orders needs to processed.

Advantages:
processers are decoupled from messaging infrastructure
Design Simplicity
supports evolutionary design independent of messaging infrastructure

Disadvantages:
Poor Load Balancing - as router is the only one which is load balanced
Poor Message Distribution - as all types of order types will come to same queue for processing
Router can be bottleneck even when load balanced.
Poor overall concurrency and scalability.

Queue Processor Design:
In Queue Processor Design we have a consumer (which is message aware) for each order type.

Advantages:
Excellent message throughput and distribution
Excellent scalability, load balancing and performance
processors can be load balanced separately, providing good flexibility.

Disadvantages:
Processors must be message aware
Evolutinary design involves expanding message infrastructure as well.

Message Priority:
*** Message Priority can lead to Unexpected results
Suppose let us consider we have 2 types of users and 3 consumers to process messages.
1. direct consumer (SLA: 5sec/msg)
2. B3B or third party consumer (SLA: 30sec/msg)


first 3 B2B orders came so it will be taken by 3 consumers.
After 1 sec 2 B2B and 5 Direct orders came 

since B2B takes 30 secs (2 B2B and 5 Direct orders has to wait for 29 secs)

After 29 secs next direct consumer orders will go to processing AS IT IS ASSIGNED WITH HIGH PRIORITY.
so wait time for 2 B2B and 2 Direct orders at this point is 34 secs (29 + 5)

After processing 2 Direct and 1 B2B will go for processing 
so wait time for 1 B2B is 39 secs (34 + 5)

Processing Times for 5 Direct Orders
1 - (29 + 5) = 34 
2 - (29 + 5) = 34
3 - (29 + 5) = 34
4 - (29 + 5 + 5) = 39
5 - (29 + 5 + 5) = 39

Processing Time for 2 B2B orders
1 - (29 + 5 + 30) = 64
2 - (29 + 5 + 5 + 30) = 69

From this one when we use Message Priority then highest priority messages WILL ALWAYS BE PROCESSED FIRST and other messages will get processing ONLY WHEN HIGH PRIORITY MESSAGES ARE NOT AVIALBLE FOR PROCESSING.
This Results in SLA issues.

To avoid this we should not use message priorities SO THAT MESSAGES WILL BE PROCESSED FIRST IN FIRST COME BASIS.
If we want to use message priorty under certain circumstances consider using SEPARATE QUEUES INSTEAD EVEN FOR SAME TYPE OF MESSAGE
means B2B one separate queue
Direct one separate queue 


JMS Transaction:
Transacted Sessions allow you to control messages during send and receive operations.

A Group of messages will be sent from sender to broker in one shot
if the sending is failed for one of the message then NO messages will go broker.

A group of messages will be received from broker to the receiver.
If processing any one of the message is failed then NO message will be deleted from the Broker.

Client Acknowledgement vs Transacted Session:

Client Acknowledgement:
Acknowledgement is by message (msg.acknowledge())
non-ack message is attached to original consumer only
** means it will not be given to other consumer if the original consumer is still connected to the broker.
** If the original consumer is down then that non-ack message will be given to other consumer.
Acknowledgement applies to consumer ONLY.

Transacted Session:
Acknowlegement is by session (session.commit())
non-ack message is available to any consumer
*** means after that transaction if it is not acknowledged due to error or by missing session.commit() statement then those messages will be available to other consumers.
*** Pitfall: Please dont forget session.commit() statement as this will lead to duplicate message processing.
Acknowledgement applies to both producer and consumer.


Embedded Broker:
useful when you need localized messaging and do not need to configure external broker and hardening the broker.

Embedded Broker helps in 
decouple components like connectors in mule esb.
resolve application bottlenecks and scalability of components by having multiple receivers to process the messages.
alternative to threads in container use messaging instead of threads.


VM Protocol
Use VM Protocol when you access broker within same jvm
Connection connection = new ActiveMQConnectionFactory("vm:embedded1").createConnection()

TCP Protocol
Use TCP Protocol when you access broker outside of  jvm
Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61888").createConnection()


Embedded Broker Considerations:
Receivers cannot start until embedded broker starts
If you dont specify persistent property on startup then all destinations and messages are removed when application terminates
consider using non-persistent messages when using embedded broker in application unless you have a strong reason 
non persistent messaging will increase performance as persistence will not happen
Be careful about other application dependencies on the embedded broker.


Queue Browser:
Message Browser is used when we to inspect messages without consuming them

Browsing is done through Queue Browser Interface.

Enumeration e = session.createQueueBrowser(queue);

Here Enumeration is a snapshot of the queue and contains all messages in the queue.

** Snapshot is created when QueueBrowser is created.
This is only available for queues (point-to-point)

Usecases:
Programmatic monitoring of queue depth and we can send out a notification if the queue depth exceeds a threshhold
Programmatic Self Healing means if the queue depth exceeds a threshhold then we can dynamically start more message listeners on the fly so that consumption/processing is faster.
Report on DLQ messages.

ConnectionMetadata:
JMS Provider information is available through ConnectionMetadata interface.
Below information is available.
JMS Version
Provider name and version
JMSX Properties supported.


Sending Images and Documents:
There are 3 techniques to send images and documents
1. File Based 
2. Payload Based
3. Streaming Based

1. File Based Technique:
In this technique the message we send to the broker ONLY CONTAINS LOCATION OF THE FILE BUT NOT THE ACTUAL FILE.

Below are the steps happened in this technique

Sender steps:
File is read from the source server.
File is stored on a temp server at the same time FILE LOCATION is sent as the message payload to the broker.

Receiver steps:
Reciever will read the message and find the file location.
File is read from the temp server.
Now File is stored the destination/target server.

*** Here Receiver will read from the temp server instead of the source server because Receiver might not be on the same network.
If both sender and receiver are on the same network then
source can send the file location as the message payload 
and receiver can read the actual file from the source server.
This improves the performances as read/write on temp server is not there.

This process is slower because it involves File IO operations.

2. Payload Based Technique
In this technique message body contains actual file contents.
steps:
sender reads the file from source system.
File content will be sent to the message broker.
Receiver will receive the message and can write the file contents on the target file.

** We can't send larger files like documents by using this technique.

3. Streaming Based Technique
In this technique we will send small file part contents as the message body.
steps:
sender sends a marker message "START" to indicate Stream start
sender receives part of file from the source system.
this file part content will be sent to the message broker.
file part content will be received by the receiver.
receiver writes this file part content to the destination server.
This process continues until complete file is received and written to the destination.
This file completion will be identified by receiving as marker message "END"

Pitfall: since we can have multiple receivers to the same queue for load balancing technique then file parts will received by multiple receivers, so final file will be corrupted.

*** To avoid this we have to JMSXGroupID so that entire file content will be received by single receiver.

Spring JMS:
There are 2 components
1. JmsTemplate
2. Listener container

JmsTemplate is used to send and receive messages synchronously.
JmsTemplate encapsulates all connection, session, producer and consumer functionality.

Listener component is used for receiving messages asynchronously
This is main component for creating message driven pojos (MDP)
MDP is a regular java class where we can use a method to receive messages asynchronously.


JndiTemplate is used to find jms broker.
JndiObjectFactoryBean - used to get jndi connection factory.
CachingConnectionFactory - this is the connection factory used by the application, this will have session objects cached similar to DB connection pool.
JndiDestinationResolver - used for creating queues and topics.

Message Drive Pojo Types
1. Message Listener
2. Message Listener Adapater

In Message Listener we have to implement MessageListener interface and implement onMessage() method to process messages.
In Message Listener Adapter we can have a normal pojo class and we can make configuration to use the pojo class as a Message Processor.


High Performant Messaging Techniques:
Using Spring Concurrent Consumers
Messaging Models
Using Multiple Queues
Message Persistence

Spring Concurrent Consumers
By increasing concurrent consumers, message processing can be improved significantly.

But how may concurrent consumers we can have?
if we use as many consumers, but below are constraints that we have
Database Connections for persistence
Memory (Heap)
CPU and Threads

Generally consumers should be in the range of 50 - 100


Messaging Models:
It is observed that messages are processed faster when we use pub sub model when compared to point to point model.
so we can use below 2 options when we use pub sub

Thread based Delegate Model - there will be one subscriber for a topic and the subscriber will delegate to the message processor.
JMS 2.0 Subscription Model - as subscribers in the subscription are load balanced.

Using Multiple Queues:
we can use multiple queues to prevent overloading of a single queue.

String[] queues = {"TRADE1.Q", "TRADE2.Q", "TRADE3.Q"};
List<QueueSender> senders = new ArrayList<>();

for(String queue: queues) {
	senders.add(session.createSender(session.createQueue(queue)));
}

index = (index == queues.length - 1)?0:(index+1);
TextMessage msg = session.createTextMessage(message);
senders.get(index).send(msg);

Message Persistence:
By Default Message are Persisted to support guaranteed delivery.
If we use Non Persistence mode then performance will be increased because persistence is not required.
we can also use JMS 2.0 Async send so that sender will not wait for the persistence acknowledgment and can execute in parallel.
