(ns rssminer.util-test
  (:use rssminer.util
        clojure.data.json
        clojure.test)
  (:require [rssminer.routes :as r])
  (:import java.util.Date
           rssminer.Utils))

(deftest test-md5-sum
  (is (= "e10adc3949ba59abbe56e057f20f883e" (md5-sum "123456"))))

(deftest test-write-json
  (is (json-str {:date (Date.)})))

(deftest test-assoc-if
  (is (= 3 (-> (assoc-if {} :a 1 :b 2 :c 3) keys count)))
  (is (= 2 (-> (assoc-if {} :a 1 :b 2 :c nil) keys count))))

(deftest test-to-init
  (is (= 3 (to-int 3)))
  (is (= 3 (to-int "3"))))

(deftest test-now-seconds
  (is (now-seconds)))

(deftest test-when-lets
  (is (= 6 (when-lets [a 1
                       [b c] [2 3]]
                      (+ a b c))))
  (is (nil? (when-lets [a false
                        [b c] [2 3]]
                       (+ a b c)))))

(deftest test-if-lets
  (is (= 6 (if-lets [a 1
                     [b c] [2 3]]
                    (+ a b c)
                    2)))
  (is (= 2 (if-lets [a false
                     [b c] [2 3]]
                    (+ a b c)
                    2)))
  (is (= 2 (if-lets [a 1
                     b false]
                    3 2)))
  (is (nil? (if-lets [a 1
                      b false]
                     3))))

(deftest test-encode-decode-key
  (let [k "zk15v22ul"]
    (is (= k (r/gen-key {:id 1})))
    (doseq [i (range 9000 9006)]
      (is (= i (r/decode-key (r/gen-key {:id i})))))))

(deftest test-should-proxy
  (is (Utils/proxy "http://feedproxy.google.com/~r/Interface21TeamBlog/~3/pwLWS9HQ6is/"))
  (is (not (Utils/proxy "http://shenfeng.me")))
  (is (Utils/proxy "http://vanillajava.blogspot.com/2012/01/another-shifty-challenge.html")))
