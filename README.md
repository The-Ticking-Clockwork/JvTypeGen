# JvTypeGen
A tool to (somewhat) serialise Java classes into JSON! Intended for generating typescript defs but currently
not implemented.

There's a few negatives to using reflection instead of `java-ts-bind` or similar, such as the fact that generics are
erased at runtime, meaning reflection cannot get those types.

Another downside is that unless you compile a jar with `-parameters` in javac, you can only get auto-generated param
names. The generated order follows `arg<N>`, meaning that the first parameter is `arg0`, the next is `arg1`, etc.