(ns example
  (:require
   [tree-sitter.parser :as p]
   [tree-sitter.tree :as t]
   [tree-sitter.node :as n]
   [coffi.mem :as mem]))

(let [session (mem/global-session)
      parser (p/make-parser :json session)]
  (->> "[1, null]"
       (#(p/parse-string parser % session))
       t/root-node
       n/children
       first))
