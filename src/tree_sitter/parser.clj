(ns tree-sitter.parser
  (:require
   [tree-sitter.library]
   [tree-sitter.types :as t]
   [tree-sitter.tree]
   [coffi.mem :as mem]
   [coffi.ffi :as ffi :refer [defcfn]]))

(defcfn ^:private delete
  "Delete the parser, freeing all of the memory that it used."
  "ts_parser_delete" [::t/TSParser*] ::mem/void)

(defcfn ^:private set-language
  "Set the language that the parser should use for parsing.

 Returns a boolean indicating whether or not the language was successfully
 assigned. True means assignment succeeded. False means there was a version
 mismatch: the language was generated with an incompatible version of the
 Tree-sitter CLI. Check the language's version using `ts_language_version`
 and compare it to this library's [[tree-sitter.defines/language-version]] and
 [[tree-sitter.defines/min-compatible-language-version]] constants."
  "ts_parser_set_language" [::t/TSParser* ::t/TSLanguage*] ::t/bool
  set-lang
  [parser lang]
  (let [fn-name (str "tree_sitter_" (name lang))]
    (when-not (set-lang parser ((ffi/cfn fn-name [] ::t/TSLanguage*)))
      (throw (ex-info "Invalid tree-sitter language" {})))
    parser))

(defcfn ^:private new'
  "ts_parser_new" [] ::t/TSParser*
  new''
  ([] (new' (mem/connected-session)))
  ([session]
   (let [parser (new'')
         gced (mem/as-segment parser 1 session)]
     (mem/add-close-action! session #(delete parser))
     gced)))

(defn make-parser
  "Create a new parser given a language.

  To load a system library that contains the grammar pass the name in as a keyword
  otherwise, pass in a fully qualified path to the shared object and the name as a keyword."
  {:arglists '([lang] [lang session] [path lang] [path lang session])}
  ([lang]
   (ffi/load-system-library (str "tree-sitter-" (name lang)))
   (set-language (new') lang))
  ([path-or-lang session-or-lang]
   (if (keyword? session-or-lang)
     ;; path lang
     (make-parser path-or-lang session-or-lang (mem/connected-session))
     ;; lang session
     (do
       (ffi/load-system-library (str "tree-sitter-" (name path-or-lang)))
       (set-language (new' session-or-lang) path-or-lang))))
  ([path lang session]
   (ffi/load-library path)
   (set-language (new' session) lang)))

(defcfn language
  "Get the parser's current language."
  "ts_parser_language" [::t/TSParser*] ::t/TSLanguage*)

;; ...

(defcfn parse-string
  "Use the parser to parse some source code stored in one contiguous buffer."
  "ts_parser_parse_string" [::t/TSParser* ::t/TSTree* ::mem/c-string ::t/uint-32] ::t/TSTree*
  parse
  ([parser source]
   (parse-string parser source (mem/connected-session)))
  ([parser source session]
   (let [tree (parse parser nil source (count (.getBytes source)))
         gced (mem/as-segment tree 1 session)]
     (mem/add-close-action! session #(#'tree-sitter.tree/delete tree))
     gced)))
