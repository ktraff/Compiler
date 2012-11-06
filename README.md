Compiler
========

A language processor for a smaller C-based toy language.  A program in the language consists of a block (surrounded by '{' '}' characters) with optional declarations and statements.  This language processor takes many examples and adaptations from the infamous "Dragon Book."  The grammar is defined as follows:

program  -> block
block    -> { decls stmt }
decls    -> decls decl  | E
decl     -> type id ;
type     -> type [ num ]  | basic
stmts    -> stmts stmt  | E
stmt     -> loc = bool ;
          | if ( bool ) stmt
          | if ( bool ) stmt else stmt
          | while ( bool ) stmt
          | do { stmt } while ( bool ) ;
          | break ;
          | block
loc      -> loc [ bool ]  | id
bool     -> bool  | | join  | join
join     -> join && equality  | equality
equality -> equality == rel | equality != rel | rel
rel      -> expr < expr | expr <= expr | expr >= expr
          | expr > expr | expr
expr     -> expr + term | expr - term | term
term     -> term * unary | term / unary | unary
unary    -> ! unary | - unary | factor
factor   -> ( bool ) | loc | num | real | true | false



