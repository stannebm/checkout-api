(ns stanne.views.layout
  (:require [hiccup.core :refer [html]]))

(defn main-layout [body]
  (html
   [:head
    [:link {:href "/tailwind.css",
            :rel "stylesheet"}]]
   [:body
    [:div.w-full.m-0.m-auto {:style "max-width: 800px"}
     [:div.bg-indigo-200
      [:h2 "Html to Hiccup"]
      [:a {:href "https://github.com/coldnew/html2hiccup"}
       "test"]
      [:p body]]]]))
