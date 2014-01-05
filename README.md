GDXMU - Miscellaneous Utilities for Libgdx.

Alpha 0.1 - initial release.

#### Getting Started
Download demos.zip and check out the basic demo.

### Downloads
Version 0.1

* [demos.zip](http://dl.bintray.com/terrywalsh123/gdxmu/gdxmu-libs.zip)
   - Contains basic and advanced demos as Eclipse projects.
* [gdxmu-all.jar](http://dl.bintray.com/terrywalsh123/gdxmu/gdxmu-all.jar)
   - Combines all gdxmu code into a single jar for you to reference in your
     code. You should remove existing references to gdx-controllers-desktop.jar
     and gdx-controllers-desktop-natives.jar because gdxmu uses its own versions
     of these jars.
* [gdxmu-libs.zip](http://dl.bintray.com/terrywalsh123/gdxmu/gdxmu-libs.zip)
   - The same as gdxmu-all except the jars are not combined. You'll need to
     reference them all in your project and remove any libgdx references with
     equivalent names.

### Notes
Gdxmu offers workarounds to desktop problems in Libgdx. I have attempted to separate the gdxmu
changes from the core Libgdx code as much as possible because I don't believe they will be merged
with Libgdx. As such I can maintain them more easily. If the Libgdx team is satisfied that the
workarounds are acceptable, then I can more closely integrate them if I am afforded a few small
changes to the Libgdx controllers code.

#### Documentation
I will provide a link to the Libgdx discussion thread on the badlogic forum when it is available.

#### Dependencies
Gdxmu makes use of the [JNA library](https://github.com/twall/jna). Version 4.0 of JNA is included
in the downloads.

#### License
Gdxmu is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you
can use it free of charge, without strings attached in commercial and non-commercial projects. This is the same
license that both Libgdx and JNA 4.0 use.

