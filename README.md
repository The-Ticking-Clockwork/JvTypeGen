# JvTypeGen
A tool to (somewhat) serialise Java classes into JSON! Intended for generating typescript defs but currently
not implemented.

There's a few negatives to using reflection instead of `java-ts-bind` or similar.
One downside is that unless you compile a jar with `-parameters` in javac, you can only get auto-generated param
names. The generated order follows `arg<N>`, meaning that the first parameter is `arg0`, the next is `arg1`, etc.

## Building
To build the project, run `./gradlew shadowJar` (or however you run a `.bat` file for Windows).

## Current State
Right now, JvTypeGen can serialise most data from classes (excluding innerclasses and generic information).
Try giving the program some classes or class descriptors, the implementation should be robust enough for it. Example
inputs (note: The semicolon should be omitted due to how Linux handles inputs, but the program can still parse them):
  - `io.github.yu_vitaqua_fer_chronos.jvtypegen.ClassInfo`
  - `Ljava/lang/String`
  - `java.lang.ClassLoader`
  - `Ljava/security/SecureClassLoader`

## Priorities
[ ] Implement generic reading (possible according to
    https://stackoverflow.com/questions/1901164/get-type-of-a-generic-parameter-in-java-with-reflection)
[ ] Fix innerclass parsing
[ ] Implement TypeScript type def generation

## Possible Goals
[ ] Add a way to recursively generate classes and interfaces
[ ] Implement javadocs parsing to fill in gaps left from reflection (such as name generation)