(ns clj-valex.core
  (:require [instaparse.core :as insta]))

(def grammar
  "
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
  symbol ::= #'[A-Z0-9/_\\-\\+\\*\\.\\$&]+'
  integer ::= #'[0-9]+'
  float ::= #'[0-9]+\\.[0-9]+(e\\-?[0-9]+)?'
  ")

(defn- strip-outer-parens [^String text]
  (let [first-paren (.indexOf text "(")
        last-paren  (.lastIndexOf text ")")]
    (subs text (inc first-paren) last-paren)))

(defn- grab [^String prefix ^String text]
  (let [idx (.indexOf text prefix (count prefix))]
    (if (neg? idx)
      text
      (subs text 0 idx))))

(defn split-seq 
  "Splits the text starting at the prefix (to include the prefix),
   returning a lazy sequence of the segments."
  [^String prefix ^String text]
  (letfn [(step [^long offset]
            (let [idx (.indexOf text prefix offset)
                  segment (grab prefix (subs text offset))]
              (if (neg? idx)
                segment
                (cons segment (lazy-seq (step (+ offset (count segment))))))))]
    (step 0)))

; Slice & dice the lex data as a lazy sequence, and process through a 
; pipeline; by default instaparse will eagerly process as much of the 
; text as possible - for very large lex data files this can take a long 
; time to process, so the preprocessing is worth it.
;
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
      (->> 
        (strip-outer-parens text)
        (split-seq "#S")
        (map (comp transform parser))))))
