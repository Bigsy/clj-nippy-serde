(ns clj-nippy-serde.serialization
  (:require [taoensso.nippy :as nippy])
  (:import [org.apache.kafka.common.serialization Serializer Deserializer Serde]))


(defn nippy-deserializer []
  (reify Deserializer
    (configure [_ _ _])
    (deserialize [_ _ data] (nippy/thaw data))
    (close [_])))

(defn nippy-serializer []
  (reify Serializer
    (configure [_ _ _])
    (serialize [_ _ data] (nippy/freeze data))
    (close [_])))

(deftype NippySerde []
  Serde
  (configure [_ configs is-key])
  (close [_])
  (serializer [_]
    (nippy-serializer))
  (deserializer [_]
    (nippy-deserializer)))

(defn nippy-serde []
  (NippySerde.))