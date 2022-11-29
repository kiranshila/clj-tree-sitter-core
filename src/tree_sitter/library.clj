(ns tree-sitter.library
  (:require [coffi.ffi :as ffi]))

;; Load the tree sitter C library
(ffi/load-system-library "tree-sitter")
