(ns tree-sitter.node
  (:require
   [tree-sitter.library]
   [coffi.mem :as mem]
   [coffi.ffi :as ffi :refer [defcfn]]
   [tree-sitter.types :as t]))

;; Pulic-facing node manipulation will always use the ->clj version
;; Where the real TSNode is in the metadata as :raw

; ...

(defcfn ^:private libc-free
  "Use libc to free a pointer"
  "free" [::mem/pointer] ::mem/void)

(defcfn as-sexpr
  "Get a node as a native s-expression"
  "ts_node_string" [::t/TSNode] ::mem/pointer
  as-str
  [node]
  (let [node (:raw (meta node))
        ptr (as-str node)
        s (mem/deserialize* ptr ::mem/c-string)]
    (libc-free ptr)
    (read-string s)))

(defcfn ^:private type'
  "Get the node's type as a keyword"
  "ts_node_type" [::t/TSNode] ::mem/c-string
  type''
  [node]
  (keyword (type'' node)))

(defcfn ^:private start-byte
  "ts_node_start_byte" [::t/TSNode] ::t/uint-32)

(defcfn ^:private start-point
  "ts_node_start_point" [::t/TSNode] ::t/TSPoint)

(defcfn ^:private end-byte
  "ts_node_end_byte" [::t/TSNode] ::t/uint-32)

(defcfn ^:private end-point
  "ts_node_end_point" [::t/TSNode] ::t/TSPoint)

(defcfn ^:private null?
  "ts_node_is_null" [::t/TSNode] ::t/bool)

(defcfn ^:private named?
  "ts_node_is_named" [::t/TSNode] ::t/bool)

(defcfn ^:private missing?
  "ts_node_is_missing" [::t/TSNode] ::t/bool)

(defcfn ^:private extra?
  "ts_node_is_extra" [::t/TSNode] ::t/bool)

(defcfn ^:private edited?
  "ts_node_has_changes" [::t/TSNode] ::t/bool)

(defcfn ^:private error?
  "ts_node_has_error" [::t/TSNode] ::t/bool)

(defcfn child-count
  "Get the node's number of children."
  "ts_node_child_count" [::t/TSNode] ::t/uint-32
  cc
  [node]
  (cc (:raw (meta node))))

(defn- ->clj [node]
  (when (not (null? node))
    ^{:raw node}
    {:type (type' node)
     :start {:byte (start-byte node)
             :point (start-point node)}
     :end {:byte (end-byte node)
           :point (end-point node)}
     :flags (into #{}
                  (comp (filter val)
                        (map key))
                  {:named (named? node)
                   :missing (missing? node)
                   :extra (extra? node)
                   :edited (edited? node)
                   :error (error? node)})}))

(defcfn child
  "Get a child of a node by its index."
  "ts_node_child" [::t/TSNode ::t/uint-32] ::t/TSNode
  c
  [node idx]
  (-> node meta :raw (c idx) ->clj))

;; TODO this crashes when we take?
(defn children
  "A lazy sequence of all the children of a node."
  [node]
  (map #(child node %) (range (child-count node))))

(defcfn next-sibling
  "Get the node's next sibling."
  "ts_node_next_sibling" [::t/TSNode] ::t/TSNode
  n
  [node]
  (-> node meta :raw n ->clj))

(defcfn prev-sibling
  "Get the node's previous sibling."
  "ts_node_prev_sibling" [::t/TSNode] ::t/TSNode
  p
  [node]
  (-> node meta :raw p ->clj))

(defcfn parent
  "Get the node's parent."
  "ts_node_parent" [::t/TSNode] ::t/TSNode
  p
  [node]
  (-> node meta :raw p ->clj))
