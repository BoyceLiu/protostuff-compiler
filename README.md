Protocol Buffers parser and code generator
------------------------------------------

[![Build Status](https://travis-ci.org/kshchepanovskyi/proto-compiler.svg?branch=master)](https://travis-ci.org/kshchepanovskyi/proto-compiler)
[![Coverity](https://scan.coverity.com/projects/5635/badge.svg)](https://scan.coverity.com/projects/5635)

Short-term Targets
------------------

1. \[parser\] Full compatibility with `proto3`.
2. \[parser\] Partial compatibility with `proto2` (groups will not be supported).
3. \[code-generator\] `proto3` template: proto-to-proto transformation, as a self-test
4. \[code-generator\] `java_nano` template: produce same output as `protoc --javanano_out=...`
5. Maven plugin

Long-term Targets
-----------------

1. Extensible plugin system that enable code generation for any language

Build
-----

```
mvn clean install
```
