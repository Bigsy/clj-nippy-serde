# clj-nippy-serde

A nippy Serde for kafka

## Usage

```clojure
(:require [clj-nippy-serde.serialization] :as :nser)

;; Create serde for kafka streams:
(def nippy-serde (nser/nippy-serde))

;; Create standard kafka serializers and deseializers
(def nippy-serializer (.serializer nippy-serde))
(def nippy-deserializer (.deserializer nippy-serde))

```

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
