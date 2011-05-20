(ns feng.rss.db.user
  (:use [feng.rss.db.util :only [exec-query select-sql-params insert-sql-params]]
        [feng.rss.util :only [md5-sum]]))

(defn create-user [user]
  (let [u (assoc user
            :password (md5-sum (:password user)))]
    (exec-query (insert-sql-params :users u))))

(defn find-user [attr]
  (first (exec-query (select-sql-params :users attr))))

(defn authenticate [email plain-password]
  (if-let [user (find-user {:email email})]
    (when (= (md5-sum plain-password) (:password user))
      user)))

