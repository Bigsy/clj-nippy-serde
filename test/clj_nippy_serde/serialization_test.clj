(ns clj-nippy-serde.serialization-test
  (:require [clojure.test :refer :all]
            [taoensso.nippy :as nippy]
            [clj-nippy-serde.serialization :as ser])
  (:import org.apache.kafka.common.serialization.Serdes
           (org.apache.kafka.streams StreamsConfig StreamsBuilder)
           (java.util Properties)
           (org.apache.kafka.common.serialization)
           (org.apache.kafka.test ProcessorTopologyTestDriver)))

(def nippy-serde (ser/nippy-serde))

(def nippy-serializer (.serializer nippy-serde))
(def nippy-deserializer (.deserializer nippy-serde))

(def byte-deserializer (.deserializer (Serdes/ByteArray)))
(def byte-serializer (.serializer (Serdes/ByteArray)))

(defn- streams-config
  []
  (let [nippyserde (-> nippy-serde .getClass .getName)
        properties (doto (new Properties)
                     (.setProperty StreamsConfig/APPLICATION_ID_CONFIG "test")
                     (.setProperty StreamsConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9091")
                     (.setProperty StreamsConfig/KEY_SERDE_CLASS_CONFIG nippyserde)
                     (.setProperty StreamsConfig/VALUE_SERDE_CLASS_CONFIG nippyserde))]
    (new StreamsConfig properties)))

(defn- simple-topology
  []
  (let [builder (new StreamsBuilder)]
    (.to (.stream builder "from") "to")
    (.build builder)))

(defn- processor-topology-test-driver
  [config topology]
  (new ProcessorTopologyTestDriver config topology))

(deftest kafka-streaming
    (let [driver (processor-topology-test-driver (streams-config) (simple-topology))
          _      (.process driver "from" "key" "value" nippy-serializer nippy-serializer)
          output (.readOutput driver "to" nippy-deserializer nippy-deserializer)]
      (is (= "key" (.key output)))
      (is (= "value" (.value output)))))

(deftest kafka-streaming-thaw
    (let [driver (processor-topology-test-driver (streams-config) (simple-topology))
          _      (.process driver "from" "key" "value" nippy-serializer nippy-serializer)
          output (.readOutput driver "to" nippy-deserializer byte-deserializer)]
      (is (= "key" (.key output)))
      (is (= "value" (nippy/thaw (.value output))))))

(deftest kafka-streaming-freeze
  (let [driver (processor-topology-test-driver (streams-config) (simple-topology))
        _      (.process driver "from" "key" (nippy/freeze "value") nippy-serializer byte-serializer)
        output (.readOutput driver "to" nippy-deserializer nippy-deserializer)]
    (is (= "key" (.key output)))
    (is (= "value" (.value output)))))