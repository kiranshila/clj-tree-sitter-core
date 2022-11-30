(ns tree-sitter.node
  (:require
   [tree-sitter.library]
   [coffi.mem :as mem]
   [coffi.ffi :as ffi :refer [defcfn]]
   [tree-sitter.types :as t]))

; ...

(defcfn libc-free
  "Use libc to free a pointer"
  "free" [::mem/pointer] ::mem/void)

(defcfn as-string
  "Get a node as a string s-expression"
  "ts_node_string" [::t/TSNode] ::mem/pointer
  as-str
  [node]
  (let [ptr (as-str node)
        s (mem/deserialize* ptr ::mem/c-string)]
    (libc-free ptr)
    s))
