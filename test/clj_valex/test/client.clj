(ns clj-valex.test.client
  (:use [clojure.test]
        [clj-valex.bz2]
        [clj-valex.core]
        ))


(def photograph (partial uncompress "resources/photograph.lex.bz2"))

(def send-lex (partial uncompress "resources/send.lex.bz2"))

; $ bzcat resources/photograph.lex.bz2 | wc 
;   3123   20697  190344
;
; ... off-by-one?  :-s

(deftest uncompress-to-list
  (is (= 3124 (count (photograph to-list)))))

(deftest uncompress-to-string
  (is (= 190345 (count (photograph to-string)))))


(comment 

(lexer "(#S(EPATTERN
             :TARGET |send|
             :SUBCAT (VSUBCAT NP)
             :CLASSES (24 5281)
             :RELIABILITY 0
             :FREQSCORE 0.0
             :RELFREQ 0.360745
             :FREQCNT 4013
             :TLTL   
             (VVD VVD VVZ VVD VV0 VV0 VV0 VVN VV0 VVG VVG VV0 VVN VV0
              VVD VVD VVD VVD VVG VVN VV0 VVD VVD VVD VVD VVD VV0 VVD
              VVN VVD VV0 VVD VVN VVD VVN VVD VVD VVD VV0 VV0 VV0 VV0
              VV0 VVD VVD VVD VVN VVN VVD VVN VVD VVN VVD VVD VVD VV0
              VVD VV0 VVD VV0 VVD VV0 VV0 VVZ VV0 VV0 VV0 VVD VV0 VVN
              VV0 VVG VVD VVD VVD VVD VVD VVG VV0 VVG VVN VV0 VV0 VV0
              VVN VVN VVD VV0 VV0 VVN VVN VV0 VV0 VV0 VVG VV0 VVG VV0
              VV0 VV0 VVG VV0 VVG VV0 VV0 VVG VVN VV0 VV0 VV0 VVN VVZ
              VV0 VVD VVD VVN VV0 VVD VVG VVD VVG VVG VVN VVD VVD VVD)
             :SLTL
             (((|suit| NN1)) ((|change| NN1)) ((|Rick| NP1))
              ((|He| PPHS1)) ((|she| PPHS1)) ((|she| PPHS1))
              ((|She| PPHS1)) ((|Teacher| NN2)) ((|who| PNQS))
              ((|language| NN1)) ((|it| PPH1)) ((|Minton| NP1))
              ((|He| PPHS1)) ((|Service| NN1)) ((|he| PPHS1))
              ((|you| PPY)) ((|he| PPHS1)) ((|They| PPHS2))
              ((|They| PPHS2)) ((|Someone| PN1)) ((|friend| NN2))
              ((|prince| NN1)))
             :OLT1L NIL
             :OLT2L NIL :OLT3L NIL :LRL 434 )
  #S(EPATTERN :TARGET |send| :SUBCAT (VSUBCAT NP_PP)
             :CLASSES (56 0) :RELIABILITY 0 :FREQSCORE 2.3301826e-25
              :RELFREQ 0.060418 :FREQCNT 0
             :TLTL :SLTL NIL :OLT1L :OLT2L NIL :OLT3L NIL :LRL 0)
        )") 

(photograph (comp lexer to-string)) 

 
(send-lex (comp lexer to-string))
  

) 
