(ns clararules.test02
  (:require [clara.rules :refer :all]))

(defrecord User [name email])
(defrecord Group [name])
(defrecord GroupUser [group-name user-name])

(defquery find-user-by-name
  [:?name]
  [?a <- :User [{name :name}] (= ?name name)])

(defquery find-group-by-name
  [:?name]
  [?a <- :Group [{name :name}] (= ?name name)])

(defquery find-user-by-group
  [:?my-group-name]
  [?groupuser <- :GroupUser [{group-name :group-name}] (= ?my-group-name group-name)]
  [?user <- :User [{name :name}] (= (:user-name ?groupuser) name)])

(def session
  (-> (mk-session 'clararules.test02
                  :fact-type-fn :type
                  :cache false)
      (insert
       {:type :User :name "Will" :email "wravel@mail.com"}
       {:type :User :name "User 2" :email "user2@mail.com"}
       {:type :User :name "User 3"  :email "user3@mail.com"}
       {:type :User :name "Client a" :email "clienta3@mail.com"}
       {:type :Group :name :home}
       {:type :Group :name :client}
       {:type :GroupUser :group-name :home :user-name "Will"}
       {:type :GroupUser :group-name :client :user-name "Client a"}
       )
      (fire-rules)))

(comment
  (query session find-user-by-name :?name "Will")
  (query session find-group-by-name :?name :home)
  (query session find-user-by-group :?my-group-name :home)
  )


