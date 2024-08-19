# SSMOS

SSM Open Source Recreation for community usage

Feel free to fork the code and edit at your leisure 

https://discord.gg/GSnKwyjyU8

# Dependencies (required)	

PandaSpigot [Repository](https://github.com/hpfxd/PandaSpigot)

Spigot 1.8.9

BuildTools [Installation](https://www.spigotmc.org/wiki/buildtools/)

# Development Env Setup

1. Download the Source Code from https://github.com/Whoneedspacee/SSMOS. (or alternatively any SSMOS fork you like)
2. Run your IDE (probably intellij) and open up the SSMOS project.
3. Allow the build scripts to initialize the developer environment.
4. Press "maven" and "reload all maven projects" button. (alternatively right-click "SSMOS" in the maven sub-menu and press "generate sources and update folders")
5. Once this process is finished, the developer environment should be ready and all your libraries compiled and ready to go.
6. If you have any issues, see the troubleshooting category below for further help.

# Compiling code & gameplay

1. Press the "maven" menu, expand the "lifecycle" tab and click the "install" button.
2. Wait for the IDE to successfully compile your code, which should end in a "BUILD SUCCESS".
3. If you want to quickly and easily test your changes, you can run the start.bat within the "ssmos-template-server", since the .jar automatically builds to the plugins folder.
3. Alternatively, under the "projects" menu, expand the "target" directory, and copy-paste the file titled "SSMOS-1.0.jar" into your desired plugins folder.

note: The template server does not come with maps pre-installed, you will have to gather these yourself. For more information, look at the readme file within that directory

# Troubleshooting

* My IDE comes up with a dependency error ("failed to download dependency", "could not find artifact", "cannot resolve `dependency-name`") and won't let me reload maven?
    * If the `dependency` that is failing is the `org.spigotmc:spigot:pom:1.8.8-R0.1-SNAPSHOT`, you need to make sure you have run through the BuildTools proccess, which can be found here: [BuildTools Installation](https://www.spigotmc.org/wiki/buildtools/).
        * The reason it fails with spigot is because the NMS code is not bundled or packaged with `panda-spigot` or the regular `spigot-api`, you are required to build and obtain that NMS code on your local machine. It's better to have a local NMS copy anyways as thats what the server is doing under-the-hood when running things, so you can research and work with it.
    * If the `dependency` that is failing is `com.hpfxd.pandaspigot:pandaspigot-api:pom:1.8.8-R0.1-SNAPSHOT` or anything `panda-spigot` related, the best thing to do is check the panda-spigot repo (linked above in the dependencies section) and make sure their maven api, repo and servers are online. It's likely they are not and that's why it cannot "fetch" the code.
    
* Classes with the import `...CraftBukkit` or `...ServerPlayer`, etc (other NMS related classes) are appearing red and not allowing me to import them?
    * This is because maven has failed to correctly load spigot NMS dependency into the project. You can fix this by opening the Maven tab, and pressing "reload all maven projects" or pressing "generate sources and update folders".
        * If the problem persists its likely you need to do a maven clean install or refresh the entire project. Maven is designed on install to just load everything from the project once and then keep it 'cached' unless an update to a dependency gets pushed. So if you're getting this error at the start, its fixable on your end. But if you are getting this error after your project is setup and you have been coding, it's likely a dependency error on maven/external repositories end. Check to make sure those dependency repositories for spigot and panda-spigot are both online.

* My `start.bat` file won't run/keeps erroring whenever I try to start the server?
    * Make sure you have Java 8 installed on your machine. 1.8 Minecraft servers and Minecraft clients need this java version in order to run, as it's an older version of the game. A quick google search should allow you to easily find an installation for this java version.

* I don't want to tamper with the `ssmos-template-server` that comes pre-installed with the code, what can I do to remedy this?
    * You don't need to directly run that template server at all. As stated above, the code can automatically compile into the /plugins directory within that template, but you can also just compile the .jar separately which will then have the file contained within the maven /target directory. My recommendation is just ctrl+copy the template-server somewhere else in your PC and just drag-drop the jar into that duplicated servers /plugins folder, that way the original template remains intact and you can modify the duplicate server however you want.  
