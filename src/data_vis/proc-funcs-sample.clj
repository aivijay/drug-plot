;;; -*- Mode: Clojure; Coding: utf-8 -*-
;;;
;;; rotate2.clj - an incanter/processing example of 'rotate'
;;;
;;; Copyright (C) 2011 by Yasuto TAKENAKA <y.takenaka@gmail.com>
;;;
;;;                       Time-stamp: <2011-06-03 20:17:30 yasuto>
;;;                       License   :  http://en.wikipedia.org/wiki/MIT_License
;;; movie -> http://www.youtube.com/watch?v=l76Uu1PfVH8

(ns data-vis.proc-funcs-sample
  (:use (incanter core processing))
  (:import [java.util.concurrent PriorityBlockingQueue]))

(defstruct obj :tr :color :time)
(defmacro dopoll [bindings & body]
  "A macro for loop manipulations of java collection queues"
  (let [var# (bindings 0)
	pq# (bindings 1)
	term# (if (= (count bindings) 2)
		`(zero? (.size ~pq#))
		`(or (zero? (.size ~pq#)) ~(bindings 2)))]
    `(when-not ~term#
       (loop [~var# (.poll ~pq#)]
	 ~@body
	 (if-not ~term#
	   (recur (.poll ~pq#)))))))
(defn rloc []
  (let [dif 180]
    (- (int (rand (* 2 dif))) dif)))

(defn rcol []
  (let [lim 256]
    (int (rand lim))))

(defn init-objs [n]
  (for [x (range n)]
    (struct obj [(rloc)(rloc)] [(rcol)(rcol)(rcol)]
	    (ref (rand 1)))))

(defn obj-comp [obj1 obj2]
  (if (< @(:time obj1) @(:time obj2)) true false))

(defn four-circle [sktch [tr-x tr-y] [r g b] rad]
  (doto sktch
    push-matrix
    (fill r g b)
    (translate tr-x tr-y)
    (rotate (radians rad))
    (ellipse -15 -15 30 30)
    (ellipse  15 -15 30 30)
    (ellipse 15 15 30 30)
    (ellipse -15 15 30 30)
    pop-matrix))

(defn fname [cunt]
  (cond
   (> 10 cunt) (str "000" cunt)
   (> 100 cunt) (str "00" cunt)
   (> 1000 cunt) (str "0" cunt)
   :else (str cunt)))

(defn draw-circle [sktch]
  (doto sktch
    (ellipse 100 100 100 100)))

(defn rotate-example2 []
  (let [objs (init-objs 30)
	pq (new PriorityBlockingQueue 10 obj-comp)
	rad (ref 0)
	cunt (ref 0)
	sktch (sketch
	       (setup []
		      (doto this
			(size 400 400)
			(background 255)
			smooth
			no-stroke
			))
	       (draw []
		     (doseq [el objs]
		       (.add pq el))
		     (doto this
		       (background 255)
		       (translate (/ (width this) 2) (/ (height this) 2))
		       (rotate (radians @rad)))
		     (dopoll [ite pq]
			     (four-circle this (:tr ite)
					  (:color ite) @rad)
                             (draw-circle this)
			     (dosync (ref-set (:time ite) (rand 1))))

;;		     (save this (str "sample" (fname @cunt) ".png"))
		     (dosync (alter rad inc)
			     (alter cunt inc))))]
    (view sktch :size [400 400] :title "rotate example")))

(rotate-example2)
