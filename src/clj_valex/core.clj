(ns clj-valex.core
  (:require [instaparse.core :as insta]))

(def grammar
  "
  <root> ::= <whitespace>? <'('> <whitespace>? 
             pattern (<whitespace> pattern)* <whitespace>?
             <')'> <whitespace>?

  pattern  ::= <whitespace>? <'#S(EPATTERN'> <whitespace>? 
               element (<whitespace> element)* <whitespace>? 
               <')'> <whitespace>?

  <element> ::= target | subcat | classes | reliability | frequency-score | 
                relative-frequency | frequency-count | tltl | sltl |
                olt1l | olt2l | olt3l | lexical-rules

  target ::= <':TARGET'> <whitespace> word
  subcat ::= <':SUBCAT'> <whitespace> list
  classes ::= <':CLASSES'> <whitespace> list
  reliability ::= <':RELIABILITY'> <whitespace> integer
  frequency-score ::= <':FREQSCORE'> <whitespace> float
  relative-frequency ::= <':RELFREQ'> <whitespace> float
  frequency-count ::= <':FREQCNT'> <whitespace> integer
  tltl ::= <':TLTL'> list
  sltl ::= <':SLTL'> (list | nil)
  olt1l ::= <':OLT1L'> (list | nil)
  olt2l ::= <':OLT2L'> (list | nil)
  olt3l ::= <':OLT3L'> (list | nil)
  lexical-rules ::= <':LRL'> <whitespace> integer

  nil ::= <whitespace> 'NIL'
  list ::= <whitespace>? | <whitespace>? <'('> <whitespace>? token (<whitespace> token)* <whitespace>? <')'>
  <token>  ::= (word | integer | float | symbol | list)
  whitespace ::= #'\\s+'
  <word> ::= <'|'> #'[^|]+' <'|'>
  symbol ::= #'[A-Z0-9/_\\-\\.\\$&]*' | '--' | '+' | '*'
  integer ::= #'[0-9]+'
  float ::= #'[0-9]+\\.[0-9]+(e\\-?[0-9]+)?'
  ")

(def lexer 
  (let [parser (insta/parser grammar)
        transform (partial insta/transform 
                           {:pattern (fn [& x] (into {} x))
                            :symbol symbol 
                            :integer read-string 
                            :float read-string 
                            :list vector 
                            :nil (fn [_] nil)})]
    (fn [text]
      (->> text parser transform))))
  
