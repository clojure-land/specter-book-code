(ns fun.book.ch1.select-both
  (:use midje.sweet commons.clojure.core))

(defn select-both [[x & xs :as selector] structure]
  (cond (empty? selector)
        (vector structure)

        (keyword? x)
        (select-both xs (get structure x))

        (fn? x)
        (if (x structure)
          (select-both xs structure)
          nil)))

(fact "works the same for keywords"
  (select-both [:a] nil) => [nil]
  (select-both [:a] :something-random) => [nil]
  (select-both [:a] {:a 1}) => [1]
  (select-both [:a] {:not-a 1}) => [nil]
  (select-both [:a] {}) => [nil]

  (select-both [:a :b] {:a {:b 1}}) => [1]
  (select-both [:a :b] {:a 1}) => [nil]
  (select-both [:a :b] {:a {}}) => [nil])

(fact "works the same for predicates"
  (select-both [odd?] 1) => [1]
  (select-both [even?] 1) => nil
  (select-both [integer? odd?] 1) => [1]
  (select-both [integer? even?] 1) => nil
  (select-both [integer? odd?] "hi") => nil)
