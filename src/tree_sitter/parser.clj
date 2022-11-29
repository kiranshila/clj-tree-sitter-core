(ns tree-sitter.parser
  (:require
   [tree-sitter.library]
   [tree-sitter.types :as t]
   [coffi.mem :as mem]
   [coffi.ffi :as ffi :refer [defcfn]]))

(defcfn new'
  "Create a new parser."
  "ts_parser_new" [] ::t/TSParser*)

(defcfn delete
  "Delete the parser, freeing all of the memory that it used."
  "ts_parser_delete" [::t/TSParser*] ::mem/void)

(defcfn set-language
  "Set the language that the parser should use for parsing.

 Returns a boolean indicating whether or not the language was successfully
 assigned. True means assignment succeeded. False means there was a version
 mismatch: the language was generated with an incompatible version of the
 Tree-sitter CLI. Check the language's version using `ts_language_version`
 and compare it to this library's [[tree-sitter.defines/language-version]] and
 [[tree-sitter.defines/min-compatible-language-version]] constants."
  "ts_parser_set_language" [::t/TSParser* ::t/TSLanguage*] ::t/bool)

(defcfn language
  "Get the parser's current language."
  "ts_parser_language" [::t/TSParser*] ::t/TSLanguage*)

;; ...

(defcfn parse-string
  "Use the parser to parse some source code stored in one contiguous buffer.

The first two parameters are the same as in the [[parse]] function.
The second two parameters indicate the location of the buffer and its length in bytes."
  "ts_parser_parse_string" [::t/TSParser* ::t/TSTree* ::mem/c-string ::t/uint-32] ::t/TSTree*)
