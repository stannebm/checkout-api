(ns stanne.views.layout
  (:require [hiccup.core :refer [html]]))

(defn layout [body]
  (html
   [:head
    [:link {:href "/tailwind.css",
            :rel "stylesheet"}]]
   [:body
    [:div.w-full.m-0.m-auto {:style "max-width: 800px"}
     body]]))
