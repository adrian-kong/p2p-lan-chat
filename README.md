# p2p-lan-chat
P2P LAN UDP chat
Basic P2P LAN chat. It works, but it might have some data loss if your internet is very slow I think.

TODO:
* Add file transfer
* Make the GUI
* Make the code better - (ordering the packets in oop classes?)

Usage
```cmd
java -jar p2p-lan-chat.jar
```
in cmd and enter your name, id and port where
* 224.0.0.0 to 224.0.0.255 is the multicast address so id is the last letter of the ip. 
* Port ranges between 0 to 65535

and it will generate a properties file with the

* name
* id
* port
* uuid
data that it will load up next time you open it. (Edit if you want to change id port or name. This project is not very versatile.)

Sending "/list" will show you all the people connected to the server which is gathered from the keep alive messages the clients send every 5 seconds
