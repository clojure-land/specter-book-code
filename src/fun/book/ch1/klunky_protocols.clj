(ns fun.book.ch1.klunky-protocols
  (:use midje.sweet commons.clojure.core))


(defprotocol StructurePath
  (select* [this remainder structure]))

(declare select)

(extend-type clojure.lang.Keyword
  StructurePath
  (select* [this remainder structure]
    (select remainder (get structure this))))

(extend-type clojure.lang.AFn
  StructurePath
  (select* [this remainder structure]
    (if (this structure)
      (select remainder structure)
      nil)))

(defn select [[x & xs :as selector] structure]
  (if (empty? selector)
    (vector structure)
    (select* x xs structure)))



(fact "works the same for keywords"
  (select [:a] nil) => [nil]
  (select [:a] :something-random) => [nil]
  (select [:a] {:a 1}) => [1]
  (select [:a] {:not-a 1}) => [nil]
  (select [:a] {}) => [nil]

  (select [:a :b] {:a {:b 1}}) => [1]
  (select [:a :b] {:a 1}) => [nil]
  (select [:a :b] {:a {}}) => [nil])

(fact "works the same for predicates"
  (select [odd?] 1) => [1]
  (select [even?] 1) => nil
  (select [integer? odd?] 1) => [1]
  (select [integer? even?] 1) => nil
  (select [integer? odd?] "hi") => nil)
