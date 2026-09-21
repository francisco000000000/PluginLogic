function sum(a, b)
  local result = a + b
  return result
end

--main, porque é o nome dele, se for abrorbura.lua e tiver como executador principal, a função chamada TAMBEM é abrorbura
function main()
    local mysum = sum(3, 3)
    print("3+3=" .. mysum)
    print("Hello!.")
end

