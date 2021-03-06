(ns speculative.string-test
  (:require
   [clojure.test :as t :refer [is deftest testing]]
   [clojure.string :as str]
   [speculative.instrument] ;; loads all specs
   [respeced.test :refer [with-instrumentation
                          with-unstrumentation
                          caught?
                          check-call]]
   [speculative.test-utils :refer [check]]
   ;; included for self-hosted cljs
   [workarounds-1-10-439.core]))

(deftest starts-with?-test
  (is (true? (check-call `str/starts-with? ["foo" "fo"])))
  #?(:clj (is (true? (check-call `str/starts-with?
                                 [(StringBuffer. "foo") "fo"]))))
  (is (false? (check-call `str/starts-with? ["foo" "ba"])))
  (with-instrumentation `str/starts-with?
    (is (caught? `str/starts-with?
                 (str/starts-with? #"foo" "bar")))
    (is (caught? `str/starts-with?
                 (str/starts-with? "foo" #"bar"))))
  (check `str/starts-with?))

(deftest ends-with?-test
  (is (true? (check-call `str/ends-with? ["foo" "oo"])))
  #?(:clj (is (true? (check-call `str/ends-with?
                                 [(StringBuffer. "foo") "oo"]))))
  (is (false? (check-call `str/ends-with? ["foo" "fo"])))
  (with-instrumentation `str/ends-with?
    (is (caught? `str/ends-with?
                 (str/ends-with? #"foo" "bar")))
    (is (caught? `str/ends-with?
                 (str/ends-with? "foo" #"bar"))))
  (check `str/ends-with?))

;;;; Scratch

(comment
  (t/run-tests)
  (stest/instrument)
  (stest/unstrument)
  )
