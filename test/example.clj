(ns example
  (:require
   [tree-sitter.parser :as p]
   [tree-sitter.node :as n]
   [tree-sitter.tree :as t]
   [coffi.mem :as mem]))

(def tree
  (let [parser (p/make-parser :json)]
    (->> "[1,null]"
         (p/parse-string parser)
         t/root-node
         #_n/as-string)))
