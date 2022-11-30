(ns example
  (:require
   [tree-sitter.parser :as p]
   [tree-sitter.tree :as t]
   [tree-sitter.node :as n]))

(def tree
  (let [parser (p/make-parser :json)]
    (->> "[1, null]"
         (p/parse-string parser)
         t/root-node
         n/as-string)))
