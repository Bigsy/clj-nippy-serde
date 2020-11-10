(defproject bigsy/clj-nippy-serde "0.1.3"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.apache.kafka/kafka-clients "1.0.1" :exclusions [org.scala-lang/scala-library]]
                 [org.apache.kafka/kafka-streams "1.0.1"]
                 [com.taoensso/nippy "2.15.3"]]

  :aot [clj-nippy-serde.serialization]

  :profiles {:dev     {:test-paths   ["test"]
                       :dependencies [[org.apache.kafka/kafka_2.12 "1.0.1" :classifier "test"]
                                      [org.apache.kafka/kafka-streams "1.0.1" :classifier "test"]
                                      [org.apache.kafka/kafka-clients "1.0.1" :classifier "test"]]}
             :uberjar {:aot :all}})
