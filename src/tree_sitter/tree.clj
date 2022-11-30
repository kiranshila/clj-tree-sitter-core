(ns tree-sitter.tree
  (:require
   [tree-sitter.library]
   [coffi.mem :as mem]
   [coffi.ffi :as ffi :refer [defcfn]]
   [tree-sitter.types :as t]))

; ..

(defcfn ^:private delete
  "Delete the syntax tree, freeing all of the memory that it used."
  "ts_tree_delete" [::t/TSTree*] ::mem/void)

(defcfn root-node
  "Get the root node of the syntax tree."
  "ts_tree_root_node" [::t/TSTree*] ::t/TSNode)
