# JvTypeGen
A tool to (somewhat) serialise Java classes into JSON! Intended for generating typescript defs but currently
not implemented.

There's a few negatives to using reflection instead of `java-ts-bind` or similar.
One downside is that unless you compile a jar with `-parameters` in javac, you can only get auto-generated param
names. The generated order follows `arg<N>`, meaning that the first parameter is `arg0`, the next is `arg1`, etc.

## Priorities
[ ] Implement generic reading (possible according to
    https://stackoverflow.com/questions/1901164/get-type-of-a-generic-parameter-in-java-with-reflection)
[ ] Fix innerclass parsing
[ ] Implement TypeScript type def generation

## Possible Goals
[ ] Add a way to recursively generate classes and interfaces
[ ] Implement javadocs parsing to fill in gaps left from reflection (such as name generation)