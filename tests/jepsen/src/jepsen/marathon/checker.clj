(ns jepsen.marathon.checker
  (:require [clojure.tools.logging :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clj-time.coerce :as tc]
            [clojure.pprint :refer [pprint]]
            [jepsen.util :as util :refer [meh]]
            [jepsen.checker :as checker]
            [jepsen.store :as store]))

(defn verify-health
  [ack-apps healthy-apps]
  (info "Acknowledged Apps: " ack-apps)
  (info "Total: " (count ack-apps))

  (info "Acknowledged Apps: " @healthy-apps)
  (info "Total: " (count @healthy-apps))
  {:valid? (= (count ack-apps) (count @healthy-apps))})

(defn marathon-checker
  "Constructs a Jepsen checker."
  [healthy-apps]
  (reify checker/Checker
    (check [this test model history opts]
      (let [ack-apps (->> history
                          (filter #(and (= :ok (:type %))
                                        (= :add-app (:f %))))
                          (map :value))]
        (info ack-apps "Total Acknowledged apps: " (count ack-apps))
        (verify-health ack-apps healthy-apps)))))