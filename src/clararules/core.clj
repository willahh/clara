(ns clararules.core
  (:require [clara.rules :refer :all]))

(defrecord SupportRequest [client level])
(defrecord ClientRepresentative [name client])

(defrecord User [name email])
(defrecord Group [name])
(defrecord GroupUser [group-name user-name])

(defrule is-will
  [User (= "Will" name)]
  =>
  (println "User is Will"))

(defrule has-email
  [User (= "wravel@mail.com" email)]
  =>
  (println "User has email"))

(defquery get-user-from-name
  [:?name]
  [?user <- User (= ?name name)])

(defquery find-group-by-name
  [:?name]
  [?group <- Group (= ?name name)])

(defquery find-user-by-group
  [:?my-group-name]
  [?groupuser <- GroupUser (= ?my-group-name group-name)]
  [?user <- User (= (:user-name ?groupuser) name)])

(def session
  (-> (mk-session 'clararules.core)
      (insert (->User "Will" "wravel@mail.com")
              (->User "User 2" "user2@mail.com")
              (->User "User 3" "user3@mail.com")
              (->User "Client a" "clienta3@mail.com")
              (->Group :home)
              (->Group :client)
              (->GroupUser :home "Will")
              (->GroupUser :client "Client a"))
      (fire-rules)))

(query session find-user-by-group :?my-group-name :home)
(query session find-user-by-group :?my-group-name :client)
(query session get-user-from-name :?name "Will")
(query session find-group-by-name :?name :home)





;; (defrule is-important
;;   "Find important support requests."
;;   [SupportRequest (= :high level)]
;;   =>
;;   (println "High support requested!"))

;; (defrule notify-client-rep
;;   "Find the client representative and request support."
;;   [SupportRequest (= ?client client)]
;;   [ClientRepresentative (= ?client client) (= ?name name)]
;;   =>
;;   (println "Notify" ?name "that"  
;;            ?client "has a new support request!"))

;; (-> (mk-session 'clararules.core)
;;     (insert (->ClientRepresentative "Alice" "Acme")
;;             (->SupportRequest "Acme" :high))
;;     (fire-rules))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
