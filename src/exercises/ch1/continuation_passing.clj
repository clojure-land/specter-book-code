(ns exercises.ch1.continuation-passing
  (:use midje.sweet commons.clojure.core))

(defprotocol Navigator
  (select* [this structure continuation]))

(extend-type clojure.lang.Keyword
  Navigator
  (select* [this structure continuation]
    (continuation (get structure this))))

(extend-type clojure.lang.AFn
  Navigator
  (select* [this structure continuation]
    (if (this structure)
      (continuation structure)
      nil)))

(defn predict-select-computation [selector]
  )

(defn select [selector structure]
  ((predict-select-computation selector) structure))




(future-fact "works the same for keywords"
  (select [:a] nil) => [nil]
  (select [:a] :something-random) => [nil]
  (select [:a] {:a 1}) => [1]
  (select [:a] {:not-a 1}) => [nil]
  (select [:a] {}) => [nil]

  (select [:a :b] {:a {:b 1}}) => [1]
  (select [:a :b] {:a 1}) => [nil]
  (select [:a :b] {:a {}}) => [nil])

(future-fact "works the same for predicates"
  (select [odd?] 1) => [1]
  (select [even?] 1) => nil
  (select [integer? odd?] 1) => [1]
  (select [integer? even?] 1) => nil
  (select [integer? odd?] "hi") => nil)

(future-facts "combining keywords and predicates"
  (select [:a map? :b] {:a 1}) => nil
  (select [:a map? :b] {:a {:b 1}}) => [1]
  (select [:a map? :b] {:a {}}) => [nil]
  (select [map? :a] {:b 1}) => [nil]
  (select [map? :a] 1) => nil)

(future-facts
  "the two forms normally return vectors (not any other kind of sequential collection"
  (select [:a :b] {:a {:b 1}}) => vector?
  (select [odd?] 1) => vector?)
