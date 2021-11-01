[33mcommit fba715671efac060532ad735bcdcfc1e04f9866f[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Nov 1 11:02:20 2021 +0100

    Fixed the tests that didnt work and also fixed the send and sync message bugs.

[33mcommit 223fb7ace277d8729dd2088a4d3e0b730bccd9fc[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 31 22:48:31 2021 +0100

    Tried to fix the send message bug.

[33mcommit 6e4f139bea2dbecdbc966d717e5f98784be4cb58[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 31 21:59:45 2021 +0100

    And now starts the painstaking bug hunting. Fixed a bug where when i tried to go into the conversation editor the program crashed

[33mcommit 60f08ed730836dc8018f24865af1f947085d0b62[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 31 21:30:49 2021 +0100

    Done refactoring so now we can start mass testing the new solutions

[33mcommit 10bfad407a4a493dfbe7b535147323f0fb2866b4[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 31 18:40:09 2021 +0100

    Major refactoring to make the project simpler and with less classes when its not really needed. Also revamping the whole network transport classes so that they are better optimized for later use. Instead of having one request its now multipule diffrent requests. Also the conversation members has been changed so that its easier to extend its function for later uses like nicknames.

[33mcommit fffe5cc38bee50fa115fdf7bc1e74bd1d91f6b44[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Oct 27 23:18:52 2021 +0200

    Fixed some documentation

[33mcommit f098e77f7c6f15ab6cd84898c9dc5dbcfc017863[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Oct 27 23:18:30 2021 +0200

    Major refactoring to reduce the amount of interfaces that is needed. Also changed the members class so that it holds members as objects and not only as names in a list.

[33mcommit 9e0c57a06a04053b83f1762e6a2e79ee5affb914[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Oct 27 00:22:43 2021 +0200

    Added some code to the server for adding new members.

[33mcommit bf21e2995f5ba167b1a40043624bca6526999e7f[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Tue Oct 26 21:15:41 2021 +0200

    Added some functionality to edit the conversation after its made.

[33mcommit 02cfa32be6d2e8cc6796421a0f7eb464d0a1bb7d[m
Merge: 87763e5 ab63609
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Tue Oct 26 12:07:25 2021 +0200

    Fixed the merge conflict

[33mcommit 87763e59abca887f27b9279eba8f8dac00d3ee69[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Tue Oct 26 12:05:52 2021 +0200

    Generalized the user and the user register some more with interfaces.

[33mcommit ab63609e3f6865c163ef7f257ef7fff132736835[m
Merge: 722b6df 73925da
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Tue Oct 26 00:02:57 2021 +0200

    Fixed the merge conflict and added a responsive chat so that the scroll pane scrolls with the messages

[33mcommit 73925dab02a3f1e6a8f8cc3de5cc2f2880b9c24f[m[33m ([m[1;31morigin/testing[m[33m)[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 25 23:14:01 2021 +0200

    Refactored some GUI code to make it better to look at. Also added all the default buttons

[33mcommit 722b6df2032551087ce1c2e6ff47234cdee7690f[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 25 22:02:30 2021 +0200

    New commit for a setting

[33mcommit c125be92bfda69c186e2941d48cd4848194d32a5[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 25 16:38:11 2021 +0200

    Hotkeyed the send button as default button

[33mcommit 290724e67447f91a95e70086473f5ac29e3f81e0[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 25 08:46:24 2021 +0200

    Started to try mutlithreading.

[33mcommit e821f5ee9c198b0e1a58ece8428e2b99385c6621[m[33m ([m[1;32mtesting[m[33m)[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Fri Oct 22 16:27:33 2021 +0200

    Now the application can look for new conversations.

[33mcommit 8a3fd857ddaa6b55cd1e1931fe2af4c20ae41684[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Thu Oct 21 23:14:51 2021 +0200

    Fixed all the tests for the personal classes. Also fixed the new conversation bug

[33mcommit 74521ac599cc219b96909734344567f6c989ddda[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Thu Oct 21 15:57:55 2021 +0200

    Added some tests and started to fix a potential exploit

[33mcommit 1720c3fd37afc354022a0ed347ab2c710b341c40[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Thu Oct 21 11:32:50 2021 +0200

    Added the methods to testingclasses for personal conversation and register.

[33mcommit a6c82ad59909502fb45165051e240ae723870aa0[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Oct 20 23:59:16 2021 +0200

    Added some documentation to the test methods

[33mcommit f3964c1004226f37100b25f4cb14876f2e822f10[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Tue Oct 19 23:21:13 2021 +0200

    Fixed the message sending bug.

[33mcommit 34e7fda18cb4e5aa105dcfd66cd313641d4f9ca0[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Tue Oct 19 21:39:34 2021 +0200

    Changed some logic on the server and chat client

[33mcommit 6f8adf7af7d4ab323fd44e79399a894d5325098f[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Tue Oct 19 21:04:41 2021 +0200

    Fixed the code so that the tests can now run again. At the same time transitioned alot of the classes into interfaces.

[33mcommit bdeb0c9ebab0f400cd66dc648b345ada6df46b07[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 18 19:05:50 2021 +0200

    Changed some documentation in the server.

[33mcommit da40e56f1b552ca72d1dafe3ee64b528c318f345[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 18 19:03:38 2021 +0200

    Major refactoring altered almost every part of the logic. The message logs are now stored in Conversations. Also added tests for many model (logic) classes.

[33mcommit 7a5f63ddfdcacfab33caa3fa01774a0b3f14f2c5[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 18 00:39:40 2021 +0200

    Refacotred most of the model code. Except for the personal conversation.

[33mcommit ae9df49a0b8b5b95810430d429a6bfa43d1d8889[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 17 19:07:13 2021 +0200

    Changed the structure. First version.

[33mcommit 75d3a61a7e284b2e61715ecc48a388e0a25aa14f[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 17 15:58:24 2021 +0200

    Added so the conversations can have names.

[33mcommit 07f57fca692e2a07c3fc461647ec12bd0a42e0a0[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Fri Oct 15 21:48:59 2021 +0200

    Fixed all the alerts and the server thread bug.

[33mcommit 853c9a8cfa03b6465b2534e8b2872d6c444b4e49[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Fri Oct 15 11:32:33 2021 +0200

    Changed some GUI stuff.

[33mcommit b03fe94a9b0c82cefa0f46b85e2b81e8df29ae87[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Fri Oct 15 10:17:59 2021 +0200

    Fixed some exceptions that didnt get thrown in the login controller

[33mcommit 526ca23f540ecf953e78761893d6f6140ca9b9a7[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Fri Oct 15 10:14:10 2021 +0200

    Added a method to make error alerts

[33mcommit 42bf30c5c68195690b4b59c4518174a3f0cc83f5[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Thu Oct 14 23:19:44 2021 +0200

    Added some alerts to the chatcontroller.

[33mcommit 42adaf23cab4629c30ee0e23bd861bf2754a381e[m
Merge: 129724c b06deff
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Thu Oct 14 17:51:28 2021 +0200

    Merge branch 'main' of https://github.com/StonedStonar/ChatApplication

[33mcommit 129724c95be365e898e3c9d373ea52a49891b786[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Thu Oct 14 17:44:14 2021 +0200

    Changed the getObject methods and refactored the code.

[33mcommit b06deff3abf47ca8794c714ca416780e0e5d1e6d[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Oct 13 22:48:14 2021 +0200

    Added a method to the observer to make the removing safer. Also moved the thread and commented some code

[33mcommit 273585ff6185b51726b3faba8548eb1f7d0b7ddb[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Wed Oct 13 17:45:04 2021 +0200

    Found a way to sync the messages thanks to my teachers code.

[33mcommit 0cb86877f46f15d418b06e43d8920dabeb83772e[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Tue Oct 12 21:57:15 2021 +0200

    Tried to add functionality so a thread could listen for messages

[33mcommit 763c133b744d19856730f4d494866b1438e96cd7[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Tue Oct 12 08:40:25 2021 +0200

    Removed the target folder

[33mcommit 2d65f166839ac87e3dc1e92690cd5330187e853a[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Tue Oct 12 00:12:29 2021 +0200

    Added the observer pattern and made a personal conversation class for the client.

[33mcommit 5042fbaf42ca781ac036c77c3f517ac2ac31d477[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 11 16:50:34 2021 +0200

    Seperated the conversation class into two classes. Personal and server conversation.

[33mcommit e3e9c83a9cb04d8795784baef4357104c5f36a1a[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 11 15:04:08 2021 +0200

    Added the function on the client and server side to make a new conversation.

[33mcommit 60514bc0a3985782c0dd46c683d767f0c7a91ef2[m
Merge: da711db 23c2a59
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 11 11:54:06 2021 +0200

    Resolved the merge conflict.

[33mcommit da711db6c4231b06d2f60a90fc34c0d9c1a75dd4[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Mon Oct 11 11:49:01 2021 +0200

    Dont have a clue what changed.

[33mcommit 23c2a593c9a073674ba3be63950bd12a684bf84d[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 11 00:22:25 2021 +0200

    Added all the functions needed for a new conversation.

[33mcommit 03c5b560b62d80f971a143201fd0e77048e2963c[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sat Oct 9 22:44:13 2021 +0200

    Added some GUI windows and controls. Also fixed a bug where conversations would stack up even though they are in the list already

[33mcommit b93b0f6a8fbf5c8a28e30d7311b136fcd469f856[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sat Oct 9 19:10:24 2021 +0200

    Refactored the whole procject so all classes throw exceptions that are logical instead of only throwing IllegalArgumentExceptions.

[33mcommit 89de0ce02d5e9271d7dc9414e2520b6e485e7214[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sat Oct 9 16:41:32 2021 +0200

    Added a logger to the chatclient and server. And started the restructure of the program so it throws the right exceptions. This is before major restructure.

[33mcommit a77096c83a4ad255e70394444343d953e4975d06[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sat Oct 9 15:37:39 2021 +0200

    Changed the look of the chat window and added so that people can change between conversations. Also fixed some bugs on the client side.

[33mcommit a3dc5431987bbf87fa353f0c56de7fe9c0f3fd26[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Fri Oct 8 18:12:54 2021 +0200

    Tried to change out the loading of a box with a text instead

[33mcommit 1940b0a259f44c3a86e293ac17d8932311c28c0d[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Fri Oct 8 12:16:02 2021 +0200

    Added some functionallity to the chat window.

[33mcommit ecece7fd6e777ecc0af76e4b1ec3d7c5018c568d[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Oct 6 22:40:25 2021 +0200

    Added the functions to login and make a new user. Also added some more logic on the server side and made the server send exceptions when something goes wrong on that side. The exceptions gets sent over the network. Also found a prototype way to get the contents of a FXML file over to a format where i can use it.

[33mcommit 366645a4f5250f3534657f6c7cb3af54a54b68ad[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Tue Oct 5 14:15:47 2021 +0200

    Fixed a bug wheret the application would not boot since the files were in another folder than the application class.

[33mcommit 18d457ba6e78feae0e47120ff36a550209103694[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 4 22:39:29 2021 +0200

    Added chatClient to the constructor.

[33mcommit ea49d577abd854098cb9a9400e553d0e2b8f871f[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Mon Oct 4 22:07:36 2021 +0200

    Added basic gui classes, fxml files and windows.

[33mcommit dd899462e0995f1c555e6806a64a10d125359e0f[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 3 23:50:42 2021 +0200

    Added some documentation to the test classes. Also added the basic setup that is needed for the GUI.

[33mcommit 81eece2f84dd1b099bc0e707280e479496e157e8[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Sun Oct 3 23:39:55 2021 +0200

    Added alot of tests and test classes. Also removed public from checking methods like checkString.

[33mcommit 65be30410e0d564dc7c500337362691cda17f233[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Fri Oct 1 00:05:59 2021 +0200

    Fixed a bug in the client

[33mcommit 059711b1fe19c77c325f5355da596fc537b515bb[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Thu Sep 30 19:25:05 2021 +0200

    Added a bunch of functionallity and some classes. Also added so that the conversation can have as many people as its needed.

[33mcommit 832367036a2e9dddbe92b34275c11d45e3160e93[m
Author: StonedStonar <steinarhjellem@gmail.com>
Date:   Wed Sep 29 23:41:47 2021 +0200

    Added a bunch of the basic classes needed for the chatting program.

[33mcommit 53cc4686ec69362dc7721f06be0c57f81929e468[m
Author: StonedStonar <Steinarhjellem@gmail.com>
Date:   Wed Sep 29 18:47:00 2021 +0200

    Initial commit
