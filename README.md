# jvx.lua

Proof-of-concept bindings for JVx/Lua.

This project contains proof-of-concept bindings for using JVx GUI classes in Lua
scripts, which can be executed at runtime.


# Usage

Add the project/jar and the necessary dependencies to a project. From there on
just create a new `LuaEnvironment` and execute scripts on it:

```java
LuaEnvironment luaEnvironment = new LuaEnvironment();
IComponent component = luaEnvironment.execute("return UIButton.new(\"Click me\")");
```


# License

jvx.lua is licensed under Apache 2.0.

The licenses of the dependencies are as follows (please see the attached license
files for further information and details):

  * Apache Commons BCEL, Apache 2.0
  * JCommon, LGPLv2.1
  * JVx, Apache 2.0
  * LuaJ, LuaJ License (BSD-like)


# Dependencies

Java 1.8 is required for this library.

Additional dependencies are:

  * [Apache Commons BCEL](https://commons.apache.org/proper/commons-bcel/index.html)
  * [JCommon](http://www.jfree.org/jcommon/)
  * [JVx](https://sourceforge.net/projects/jvx/)
  * [LuaJ](http://www.luaj.org/luaj.html)
