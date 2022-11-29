(ns example
  (:require
   [coffi.ffi :as ffi :refer [defcfn]]
   [tree-sitter.parser :as p]
   [tree-sitter.types :as t]
   [coffi.mem :as mem]))

;; Load the grammar
(ffi/load-system-library "tree-sitter-json")

;; Declare the language function
(defcfn json "tree_sitter_json" [] ::t/TSLanguage*)

;; Create a parser and set it's language
(def parser
  (let [ps (p/new')]
    (when (p/set-language ps (json))
      ps)))

;; Build a syntax tree based on some source code
(def tree
  (let [source "[1, null]"]
    (p/parse-string parser nil source (count source))))
