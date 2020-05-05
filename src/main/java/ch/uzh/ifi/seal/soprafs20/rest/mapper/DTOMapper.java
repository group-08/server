package ch.uzh.ifi.seal.soprafs20.rest.mapper;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.persistence.MappedSuperclass;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "gameState", target = "gameState")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "exchangeCard", target = "exchangeCard")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "cardNum", target = "cardNum")
    @Mapping(source = "weatherState", target = "weatherState")
    @Mapping(source = "board", target = "board")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "id", target = "id")
    FieldGetDTO convertEntityToFieldGetDTO(Field field);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "fields", target = "fields")
    BoardGetDTO convertEntityToBoardGetDTO(Board board);










}
