![mapstruct-2047561](https://github.com/user-attachments/assets/f61f0e71-c3a7-4351-ad7e-ad5d885fb4fc)
<h1>Facilitando a conversão de DTOs com MapStruct no desenvolvimento Java</h1>

No desenvolvimento de aplicações Java, uma prática comum é o uso de Data Transfer Objects (DTOs). DTOs são utilizados para transportar dados entre as camadas de uma aplicação, ajudando a separar as entidades de domínio da lógica de apresentação e controle, o que resulta em um código mais limpo e seguro. Contudo, a conversão entre entidades e DTOs pode ser repetitiva e propensa a erros, especialmente em sistemas com grande quantidade de dados e relacionamentos complexos.

Para simplificar esse processo, o MapStruct é uma biblioteca que se destaca. O MapStruct oferece uma maneira simples e eficiente de fazer o mapping entre objetos, criando automaticamente os métodos de conversão entre as entidades e seus respectivos DTOs. Esse framework é baseado em anotações e gera o código de mapeamento em tempo de compilação, proporcionando maior desempenho e menor necessidade de código escrito manualmente.

## Como o MapStruct Funciona Internamente

O MapStruct funciona como um processador de anotações que gera código-fonte no momento da compilação. Quando você define uma interface de mapeamento, anotada com @Mapper, o MapStruct analisa essa interface e cria uma implementação concreta com os métodos que convertem os objetos conforme especificado.

Internamente, ele verifica os nomes e tipos dos atributos das classes de origem e destino, realizando as associações de maneira automática, desde que os atributos correspondam entre as duas classes. Caso os nomes dos atributos sejam diferentes, o MapStruct permite a personalização através de configurações e anotações adicionais, como @Mapping, para definir correspondências específicas entre os campos.


```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

}
```

- `componentModel = MappingConstants.ComponentModel.SPRING`: Integra o mapper com o Spring, permitindo que ele seja injetado como um bean.
- `unmappedTargetPolicy = ReportingPolicy.ERROR`: Emite erro de compilação caso existam propriedades na classe de destino que não estejam mapeadas.


***

O campo *componentModel = MappingConstants.ComponentModel.SPRING* na anotação @Mapper configura o MapStruct para integrar o mapper gerado com o Spring Framework. Quando definimos componentModel como SPRING, estamos dizendo ao MapStruct para gerar a classe de implementação do mapper como um bean gerenciado pelo Spring.

isso significa que:

O mapper será registrado automaticamente no contexto de injeção de dependência do Spring.
Podemos injetar o mapper em outras classes Spring usando a anotação @Autowired.



Já o campo *unmappedTargetPolicy = ReportingPolicy.ERROR* na anotação @Mapper configura o MapStruct para emitir um erro de compilação caso existam propriedades na classe de destino que não estejam mapeadas.

Quando usamos ReportingPolicy.ERROR, o MapStruct verifica se todos os atributos da classe de destino têm uma correspondência no mapeamento. Se algum campo ficar sem mapeamento, o compilador gera um erro, forçando o desenvolvedor a mapear todos os atributos explicitamente.

Essa configuração ajuda a garantir que nenhum dado importante seja deixado de fora durante a conversão e é especialmente útil em projetos onde a consistência e integridade dos dados são fundamentais.

## Modelo de dominío(Domain model)

```java
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;

    public User() {
    }

//Construtor com argumento
//Getters e Setters
//HashCode and equals(id) 
```

***

## Classes Record para conversão de DTOs

```java

public record UserRequestDto(@NotBlank String nome,
                             @Email String email,
                             @NotBlank String senha) {
}
```
Este é o nosso DTO do tipo record para receber o corpo da requisição do usuário.

***

```java

public record UserResponseDto(@NotBlank String nome,
                              @Email String email) {
}
```

Este será o nosso DTO que vai tratar os dados enviados pelo usuário e retornar apenas aqueles atributos ao qual desejamos expor, neste exemplo irei retornar ao corpo apenas o nome é o email do usuário.



***

## Interface UserMapper(Responsável pelo mapeamento do MapStruct)

```java


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    User converterParaEntidadeUser(UserRequestDto dto);

    UserResponseDto converterParaUserResposta(UserRequestDto requestDto);

    @Mapping(target = "User.id", ignore = true)
    UserRequestDto converterParaUserRequestDTO(User user);

    List<UserResponseDto> converterParaListaUser(List<User> users);
}

```

Nesta interface, utilizamos o MapStruct para automatizar a conversão entre as classes de domínio e DTOs, especificamente User, UserRequestDto, e UserResponseDto. Essa interface fornece métodos de mapeamento que facilitam a transformação de dados de maneira automática e eficiente, eliminando a necessidade de escrever código de conversão manual.

- Método converterParaEntidadeUser(UserRequestDto dto)

```java
@Mapping(target = "id", ignore = true)
    User converterParaEntidadeUser(UserRequestDto dto);
```

Este método converte um UserRequestDto para a entidade User. A anotação @Mapping(target = "id", ignore = true) indica que o campo id na entidade User será ignorado durante o mapeamento. Isso é útil quando o id é gerado automaticamente pelo banco de dados(Anotação @GeneratedValue (strategy = GenerationType.IDENTITY)), como ao criar um novo usuário. Assim, ao converter de UserRequestDto para User, garantimos que o id permaneça nulo, permitindo que o banco o preencha quando necessário.

- Método converterParaUserResposta(UserRequestDto dto)

```java
UserResponseDto converterParaUserResposta(UserRequestDto requestDto);

```
Esse método converte um UserRequestDto diretamente para um UserResponseDto. O MapStruct gerará automaticamente o código necessário para o mapeamento de todos os campos com o mesmo nome e tipo entre os dois objetos. Este método é útil para transformar dados do usuário de entrada em uma resposta de saída adequada, como após a criação ou atualização de um usuário.

- Método converterParaUserResquestDto(User user)

```java
@Mapping(target = "User.id", ignore = true)
    UserRequestDto converterParaUserRequestDTO(User user);
```
Este método converte uma entidade User em um UserRequestDto. Novamente, usamos @Mapping(target = "id", ignore = true) para garantir que o campo id seja ignorado ao fazer essa conversão. Essa configuração é útil, por exemplo, quando estamos preparando um DTO para uma atualização de dados do usuário onde o id não precisa ser enviado ao frontend ou manipulado pelo cliente.

- Método converterParaListaUser(List<User> users)

```java
List<UserResponseDto> converterParaListaUser(List<User> users);

```
Este método converte uma lista de objetos User em uma lista de UserResponseDto. O MapStruct identifica a lista como um tipo genérico e gera automaticamente o código necessário para mapear cada elemento de User para UserResponseDto. Essa funcionalidade é muito útil para cenários em que precisamos retornar uma lista de usuários convertida para um formato específico, como em respostas de endpoints que retornam vários registros.

***

## Vantagens da Interface UserMapper

Ao definir uma interface de mapeamento como UserMapper, o MapStruct nos permite:

- Evitar código repetitivo: Reduz o número de conversões manuais, tornando o código mais limpo e legível.
- Automatizar mapeamentos: A geração automática de código pelo MapStruct reduz a chance de erros.
- Manter a consistência de dados: Ao ignorar campos como id onde apropriado, garantimos que dados gerenciados pelo banco (como identificadores) não sejam manipulados de forma indesejada.

***

##Classe de serviço 

```java
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDto criar(UserRequestDto requestDto) {
        return userMapper.converterParaUserResposta(userMapper
                .converterParaUserRequestDTO(userRepository
                        .save(userMapper.converterParaEntidadeUser(requestDto))));
    }

    public UserResponseDto atualizarNomeEmail(Long id, UserResponseDto dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setNome(dto.nome());
                    user.setEmail(dto.email());
                    return userMapper.converterParaUserResposta(userMapper.converterParaUserRequestDTO(userRepository.save(user)
                    ));
                }).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado, " + id));
    }

    public void deletar(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado, " + id));
        userRepository.delete(user);
    }

    public List<UserResponseDto> listar() {
        return userMapper.converterParaListaUser(userRepository.findAll());
    }

    public UserResponseDto buscar(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não existe."));
        return userMapper.converterParaUserResposta(userMapper.converterParaUserRequestDTO(user));
    }
} 
```

***

## Classe controller

```java
 @RestController
@RequestMapping(value = "users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> criar(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.criar(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> atualizar(@PathVariable long id, @Valid @RequestBody UserResponseDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.atualizarNomeEmail(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listar() {
        return ResponseEntity.ok().body(userService.listar());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> buscar(@PathVariable long id) {
        return ResponseEntity.ok().body(userService.buscar(id));
    }
}
```
Podemos observar que, graças ao framework MapStruct, evitamos expor diretamente nosso modelo de domínio (User), deixando o código mais encapsulado e seguro. Embora ainda haja muitos aspectos a serem explorados, o foco deste artigo é mostrar como utilizar esse framework poderoso em nosso dia a dia de desenvolvimento.

***

## Conclusão
O MapStruct é uma ferramenta poderosa para simplificar o mapeamento entre objetos no desenvolvimento Java, tornando o código mais claro, eficiente e menos propenso a erros. Neste artigo, exploramos os conceitos básicos de configuração e uso de mappers para transformar dados entre DTOs e entidades. Adotar o MapStruct em projetos pode facilitar a manutenção e promover boas práticas de organização e encapsulamento.

Espero que este conteúdo tenha sido útil e incentivador para você implementar o MapStruct no seu dia a dia. Caso tenha dúvidas ou queira compartilhar suas próprias experiências com o MapStruct, fique à vontade para deixar um comentário.

*Desenvolvedor Jr, Ricardo Barrote.*

