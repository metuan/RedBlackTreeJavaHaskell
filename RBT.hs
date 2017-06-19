module RBTree (Tree, empty, insert, flatten, treeUniqueSort) where

data Tree a = Red (Tree a) a (Tree a) | Black (Tree a) a (Tree a) | Leaf

empty :: Tree a
empty = Leaf

insert :: Ord a ⇒ a → Tree a → Tree a
insert e t =
   case ins t of
      Red a x b → Black a x b
      t → t
   where
      ins Leaf = Red Leaf e Leaf
      ins (Red   a x b) = nav Red   a x b
      ins (Black a x b) = nav black a x b
      nav root a x b =
         case e `compare` x of
            LT → root (ins a) x b
            EQ → root a e b
            GT → root a x (ins b)
      black (Red a x (Red b y c)) z d = Red (Black a x b) y (Black c z d)
      black (Red (Red a x b) y c) z d = Red (Black a x b) y (Black c z d)
      black a x (Red b y (Red c z d)) = Red (Black a x b) y (Black c z d)
      black a x (Red (Red b y c) z d) = Red (Black a x b) y (Black c z d)
      black a x b = Black a x b

flatten :: Tree a → [a]
flatten = aux [] where
   aux acc Leaf = acc
   aux acc (Red   a x b) = aux (x : aux acc b) a
   aux acc (Black a x b) = aux (x : aux acc b) a

treeUniqueSort :: Ord a ⇒ [a] → [a]
treeUniqueSort = flatten . foldr insert empty