(ns rssminer.redis
  (:use [rssminer.config :only [rssminer-conf]])
  (:import [redis.clients.jedis JedisPool Jedis JedisPoolConfig]))

(defonce redis-pool (atom nil))

(defn set-redis-pool! [^String host] ;; called when app start
  (if-let [p ^JedisPool @redis-pool]
    (.destroy p))
  (let [pool (JedisPool. (JedisPoolConfig.) host)]
    (swap! rssminer-conf assoc :redis-server pool)
    (reset! redis-pool pool)))

(def ^String fetcher-key "fetcher-queue")

(def ^"[Ljava.lang.String;" fetcher-key-arr (into-array (list fetcher-key)))

(defn fetcher-enqueue [data]
  (let [^JedisPool client @redis-pool
        ^Jedis j (.getResource client)]
    (try
      (.rpush j fetcher-key ^"[Ljava.lang.String;"
              (into-array (list
                           (pr-str data)))) ; tail
      (finally (.returnResource client j)))))

(defn fetcher-dequeue [timeout]         ; seconds
  (let [^JedisPool client @redis-pool
        ^Jedis j (.getResource client)]
    (try
      (when-let [d (.blpop j (int timeout) fetcher-key-arr)]
        (-> d second read-string))
      (finally (.returnResource client j)))))
