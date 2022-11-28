# Introducing Tagless Final

See https://scalac.io/tagless-final-pattern-for-scala-code

Tagless Final allows you to build a subset of the host language which is sound, typesafe and predictable.

When designed properly this subset makes it easy to write correct programs and hard to write incorrect ones.
In fact, invalid states can’t be expressed at all! Later, the solution written in the hosted language is safely run to
return the value to the hosting language.

For us Scala will be our hosting language, but the pattern also applies to other ecosystems. Tagless Final can be seen
as composite pattern, it builds on top of or is inspired by many other patterns like type classes or Free monads. So you
might spot some similarities. On a conceptual level our Scala implementation has 3 distinct parts:

* Language, that defines a subset of operations that the hosted language allows
* Bridges, helpers that express Scala values and business logic in the Language
* Interpreters, dual to Bridges they run logic expressed as the Language and get the final value

This is not the official lingo, but I will be using this naming here to make explanations simpler.