

a service is raised that allows, upon request, to send data to the broker and, upon request, receive one message from the queue
settings for connecting to the broker and setting up the incoming message port are in the file MyRouteBuilder

before the message is placed in the queue, it is converted from xml to json(imitation of useful work)

I tested with AB:
put a message in the broker: ab -n 99999999 -c 5 -s 200 -T 'text/xml;charset=UTF-8' -p payload.xml http://localhost:8000/put
get message from broker: ab -n 99999999 -c 20 -s 200 http://localhost:8000/get

after a certain time it will be seen that the memory will leak
