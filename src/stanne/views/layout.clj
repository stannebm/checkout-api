(ns stanne.views.layout
  (:require [hiccup.core :refer [html]]))

(defn layout [body]
  (html
   [:head
    [:link {:rel "preconnect",
            :href "https://fonts.gstatic.com"}]
    [:link {:href "https://fonts.googleapis.com/css2?family=Lato:wght@300&display=swap",
            :rel "stylesheet"}]
    [:link {:href "https://fonts.googleapis.com/css2?family=Montserrat:wght@300;500&display=swap",
            :rel "stylesheet"}]
    [:link {:href "/tailwind.css",
            :rel "stylesheet"}]]
   [:body
    [:div {:style {:background "linear-gradient(to bottom, #2D3748 0%, #2C7A7B 100%)"}}
     [:div.w-full.m-0.m-auto.min-h-screen.py-10.px-3 {:style "max-width: 620px"}
      [:div.my-10.rounded-xl {:style {:background "#f9f9f9"
                                      :font-family "Montserrat"
                                      :font-weight "300"}}
       body]]

     [:div.w-full.bottom-0.absolute {:style {:font-family "Montserrat"
                                             :font-weight 300
                                             :font-size "14px"
                                             :color "#EDF2F7"}}
      [:div.flex.justify-center {:style {:padding "20px"}}
       [:p "&copy; Copyright 2020-2021 minorbasilicastannebm.com. All Rights Reserved."]]]]]))
