(ns example
  (:require
   [tree-sitter.parser :as p]
   [tree-sitter.tree :as t]
   [tree-sitter.node :as n]
   [coffi.mem :as mem]))

(def tree
  (with-open [session (mem/stack-session)]
    (let [parser (p/make-parser :json session)]
      (-> "[1, null]"
          (#(p/parse-string parser % session))
          t/root-node
          n/as-string
          read-string))))
