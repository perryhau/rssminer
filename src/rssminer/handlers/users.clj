(ns rssminer.handlers.users
  (:use  [ring.util.response :only [redirect]]
         [ring.middleware.file-info :only [make-http-format]])
  (:require [rssminer.db.user :as db]
            [rssminer.views.users :as view])
  (:import [java.util Locale Calendar TimeZone Date]
           java.text.SimpleDateFormat))

(defn- get-expire "get string for http expire header" [days]
  (let [^SimpleDateFormat f (make-http-format)
        c (doto (Calendar/getInstance)
            (.add Calendar/DAY_OF_YEAR days))
        d (.getTime c)]
    (.format f d)))

(defn show-login-page [req]
  (view/login-page "/app"))

(defn login [req]
  (let [{:keys [email password return-url persistent]} (:params req)
        user (db/authenticate email password)
        return-url (or return-url "/app")]
    (if user
      (assoc (redirect return-url)
        :session {:user user}
        :session-cookie-attrs (if persistent
                                {:expires (get-expire 7)
                                 :http-only true}
                                {:http-only true}))

      (view/login-page return-url))))

(defn show-signup-page [req]
  (view/signup-page))

(defn signup [req]
  (let [{:keys [email password]} (:params req)
        user (db/create-user {:email email
                              :password password})]
    (assoc (redirect "/app")
      :session {:user user})))
