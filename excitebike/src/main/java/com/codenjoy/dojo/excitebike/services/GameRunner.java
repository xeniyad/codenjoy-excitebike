package com.codenjoy.dojo.excitebike.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.excitebike.client.Board;
import com.codenjoy.dojo.excitebike.client.ai.AISolver;
import com.codenjoy.dojo.excitebike.model.items.GameElementType;
import com.codenjoy.dojo.excitebike.model.GameFieldImpl;
import com.codenjoy.dojo.excitebike.model.Level;
import com.codenjoy.dojo.excitebike.model.LevelImpl;
import com.codenjoy.dojo.excitebike.model.Player;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeElementType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.google.common.collect.ObjectArrays;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 */
public class GameRunner extends AbstractGameType implements GameType {

    //TODO: move it to the Board class
    public static final int EMPTY_LINES_ON_TOP = 3;
    public static final int FIELD_LENGTH = 38;

    private final Level level;

    public GameRunner() {
        level = new LevelImpl(getMap());
    }

    protected String getMap() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < EMPTY_LINES_ON_TOP; i++) {
            appendElementManyTimes(sb, GameElementType.NONE, FIELD_LENGTH);
        }
        appendElementManyTimes(sb, GameElementType.BORDER, FIELD_LENGTH);
        appendBikeAtStartPoint(sb);
        appendBikeAtStartPoint(sb);
        appendElementManyTimes(sb, GameElementType.BORDER, FIELD_LENGTH);
        return sb.toString();
    }

    private void appendElementManyTimes(StringBuilder sb, GameElementType element, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(element);
        }
    }

    private void appendBikeAtStartPoint(StringBuilder sb) {
        sb.append(GameElementType.ROAD);
        sb.append(BikeElementType.BIKE_BACK);
        sb.append(BikeElementType.BIKE_FRONT);
        appendElementManyTimes(sb, GameElementType.ROAD, FIELD_LENGTH - 3);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        // nothing to implement
        return null;
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new GameFieldImpl(level, getDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "excitebike";
    }

    @Override
    public Enum[] getPlots() {
        Enum[] tempArr = ObjectArrays.concat(GameElementType.values(), SpringboardElementType.values(), Enum.class);
        return ObjectArrays.concat(tempArr, BikeElementType.values(), Enum.class);
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        return new Player(listener);
    }
}
