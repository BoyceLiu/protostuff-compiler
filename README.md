Protocol Buffers parser and code generator
------------------------------------------

[![Build Status](https://travis-ci.org/kshchepanovskyi/protostuff-compiler.svg?branch=master)](https://travis-ci.org/kshchepanovskyi/protostuff-compiler)
[![Coverity](https://scan.coverity.com/projects/5635/badge.svg)](https://scan.coverity.com/projects/5635)

Targets
-------

Ordered by priority:

1. [Full compatibility with proto3](https://github.com/kshchepanovskyi/protostuff-compiler/issues/1)
2. [Compatibility with proto2](https://github.com/kshchepanovskyi/protostuff-compiler/issues/2)
3. [Command-line interface](https://github.com/kshchepanovskyi/protostuff-compiler/issues/3)
4. [HTML generator template](https://github.com/kshchepanovskyi/protostuff-compiler/issues/4)
5. [Maven plugin](https://github.com/kshchepanovskyi/protostuff-compiler/issues/5)
6. [Proto template](https://github.com/kshchepanovskyi/protostuff-compiler/issues/6)
7. [java_nano template](https://github.com/kshchepanovskyi/protostuff-compiler/issues/7)
8. [Generator extensions](https://github.com/kshchepanovskyi/protostuff-compiler/issues/8)


Build
-----

```
$ mvn clean install
```

Maven Plugin
------------

Requirements: Maven 3.1.0+

HTML Template
-------------

Note for google chrome: if you want to use generated files without running web server,
then you should run it with a special parameter:

```
$ chrome --allow-file-access-from-files index.html
```
