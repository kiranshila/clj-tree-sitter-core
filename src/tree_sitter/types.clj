(ns tree-sitter.types
  (:require
   [coffi.mem :as mem :refer [defalias]]
   [coffi.layout :refer [with-c-layout]]))

;; Pretend unsigned ints

(defmethod mem/primitive-type ::uint-16
  [_type]
  ::mem/short)

(defmethod mem/serialize* ::uint-16
  [obj _type session]
  (unchecked-short obj))

(defmethod mem/deserialize* ::uint-16
  [obj _type]
  (Short/toUnsignedInt obj))

(defmethod mem/primitive-type ::uint-32
  [_type]
  ::mem/int)

(defmethod mem/serialize* ::uint-32
  [obj _type session]
  (unchecked-int obj))

(defmethod mem/deserialize* ::uint-32
  [obj _type]
  (Integer/toUnsignedLong obj))

(defmethod mem/primitive-type ::bool
  [_type]
  ::mem/int)

(defmethod mem/serialize* ::bool
  [obj _type session]
  (if obj 1 0))

(defmethod mem/deserialize* ::bool
  [obj _type]
  (not (zero? obj)))

;; Aliases

(defalias ::TSSymbol ::uint-16)
(defalias ::TSFieldId ::uint-16)
(defalias ::TSLanguage* ::mem/pointer)
(defalias ::TSParser* ::mem/pointer)
(defalias ::TSTree* ::mem/pointer)
(defalias ::TSQuery* ::mem/pointer)
(defalias ::TSQueryCursor* ::mem/pointer)

(defalias ::TSInputEncoding
  [::mem/enum
   [:TSInputEncodingUTF8
    :TSInputEncodingUTF16]])

(defalias ::TSSymbolType
  [:mem/enum
   [:TSSymbolTypeRegular
    :TSSymbolTypeAnonymous
    :TSSymbolTypeAuxiliary]])

(defalias ::TSPoint
  (with-c-layout
    [::mem/struct
     [[:row ::uint-32]
      [:column ::uint-32]]]))

; ..

(defalias ::TSNode
  (with-c-layout
    [::mem/struct
     [[:context [::mem/array ::uint-32 4]]
      [:id ::mem/pointer]
      [:tree ::TSTree*]]]))
